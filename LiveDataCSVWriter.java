import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.BiConsumer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.concurrent.Task;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.control.ProgressBar;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * The `LiveDataCSVWriter` class is responsible for fetching live air pollution data for specified pollutants
 * (NO2, PM10, and PM2.5) from the World Air Quality Index (WAQI) API. The data is written to a DEFRA-compliant CSV file
 * which includes various details such as grid codes, easting, northing, and pollution values. The CSV files are stored
 * in respective pollutant folders under the "UKAirPollutionData" directory.
 * 
 * @author Ozgur Dorunay
 * @version 24/03/25
 */

public class LiveDataCSVWriter {

    private static final String BASE_URL = "https://api.waqi.info/feed/geo:";
    private static final String TOKEN = "a31e7701223e9235c016f444a4349dc40e0240af"; 

    /**
     * Fetches pollution data from WAQI API and writes it to a DEFRA-compliant CSV file.
     * Deletes the old file first, then writes all new data.
     * @param locations List of locations (gridcode, easting, northing, lat, lon)
     * @param pollutant The pollutant type ("no2", "pm10", "pm25")
     * @param onComplete Callback function to handle completion (String year, String pollutant)
     * @throws IOException
     * @throws InterruptedException
     */
    public static void fetchAndWriteCSV(List<double[]> locations, String pollutant, BiConsumer<String, String> onComplete) 
    throws IOException, InterruptedException {

        // Determine file name and folder
        String pollutantFolder = pollutant.equals("no2") ? "NO2" : (pollutant.equals("pm25") ? "pm2.5" : pollutant);
        String folder = "UKAirPollutionData/" + pollutantFolder + "/";
        String filename = folder + "map" + pollutant + "live" + ((pollutant.equals("pm10") || pollutant.equals("pm25")) ? "g" : "") + ".csv";

        // Delete existing file once before writing new data
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
            System.out.println("Deleted old file: " + filename);
        }

        // Create a new file and write DEFRA headers
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(getPollutantName(pollutant));  // Line 1: Pollutant Name
            writer.println("live");                       // Line 2: Year
            writer.println("annual mean");               // Line 3: Metric 
            writer.println("ug m-3");                     // Line 4: Units
            writer.println();                            // Line 5: Blank line
            writer.println("ukgridcode,x,y,value"); // Line 6: Column headers
        }

        // Write each location's pollution data
        AtomicInteger lineCount = new AtomicInteger(0);
        ProgressBar progressBar = new ProgressBar(0);
        Label progressLabel = new Label("Starting...");

        // Create a background task to avoid freezing the UI
        Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    int totalLocations = locations.size();

                    for (int i = 0; i < totalLocations; i++) {
                        double[] loc = locations.get(i);
                        int gridcode = (int) loc[0];
                        int easting = (int) loc[1];
                        int northing = (int) loc[2];
                        double lat = loc[3];
                        double lon = loc[4];

                        // Fetch pollution data
                        String value = fetchPollutionValue(lat, lon, pollutant);

                        // Append to the file
                        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
                            writer.println(gridcode + "," + easting + "," + northing + "," + value);
                            int currentCount = lineCount.incrementAndGet();
                            double progress = (double) currentCount / totalLocations;

                            // Update the progress bar and label on the JavaFX Application Thread
                            Platform.runLater(() -> {
                                        progressBar.setProgress(progress);
                                        progressLabel.setText("Lines written: " + currentCount + " / " + totalLocations);
                                });
                        } catch (IOException e) {
                            System.out.println("Error writing to CSV: " + e.getMessage());
                        }
                    }

                    // After writing, call the completion callback
                    Platform.runLater(() -> {
                                if (onComplete != null) {
                                    onComplete.accept("live", pollutant);
                                }
                        });

                    return null;
                }
            };

        // Display progress bar in a window
        Platform.runLater(() -> {
                    Stage progressStage = new Stage();
                    VBox vbox = new VBox(10, progressLabel, progressBar);
                    Scene scene = new Scene(vbox, 300, 100);
                    progressStage.setScene(scene);
                    progressStage.setTitle("CSV Writing Progress");
                    progressStage.show();

                    // Close the stage when the task is done
                    task.setOnSucceeded(e -> progressStage.close());
                    task.setOnFailed(e -> progressStage.close());
            });

        // Run the task in a new thread
        new Thread(task).start();
        
        onComplete.accept("live", pollutant);

        System.out.println("DEFRA-compliant CSV file created: " + filename);
    }

    /**
     * Fetch pollution value for a specific pollutant from WAQI API.
     */
    private static String fetchPollutionValue(double lat, double lon, String pollutant) throws IOException, InterruptedException {
        String url = BASE_URL + lat + ";" + lon + "/?token=" + TOKEN;

        // System.out.println("Fetching URL: " + url); used for debugging

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println("Response: " + response.body()); //used for debugging

        if (response.statusCode() == 200) {
            JsonElement jsonElement = JsonParser.parseString(response.body());
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.get("status").getAsString().equals("ok")) {
                JsonObject iaqi = jsonObject.getAsJsonObject("data").getAsJsonObject("iaqi");
                return iaqi.has(pollutant) ? iaqi.getAsJsonObject(pollutant).get("v").getAsString() : "MISSING";
            }
        }
        return "MISSING";
    }

    /**
     * Returns the full pollutant name as required by DEFRA headers.
     * @param pollutant The pollutant code ("no2", "pm10", "pm25")
     * @return Full pollutant name
     */
    private static String getPollutantName(String pollutant) {
        return switch (pollutant) {
            case "no2" -> "NO2";
            case "pm10" -> "PM10";
            case "pm25" -> "PM2.5";
            default -> "Unknown Pollutant";
        };
    }

    /**
     * Fetches pollution data and outputs it to the terminal. Used for debugging.
     * @param locations List of locations (gridcode, easting, northing, lat, lon)
     * @param pollutant The pollutant type ("no2", "pm10", "pm25")
     * @throws IOException
     * @throws InterruptedException
     */
    public static void outputToTerminal(List<double[]> locations, String pollutant)
    throws IOException, InterruptedException {

        System.out.println("---- " + getPollutantName(pollutant) + " Live Data ----\n");

        for (double[] loc : locations) {
            int gridcode = (int) loc[0];
            int easting = (int) loc[1];
            int northing = (int) loc[2];
            double lat = loc[3];
            double lon = loc[4];

            // Fetch pollution data
            String value = fetchPollutionValue(lat, lon, pollutant);

            // Output directly to terminal
            System.out.printf("Gridcode: %d | Easting: %d | Northing: %d | Value: %s\n",
                gridcode, easting, northing, value);
        }
        System.out.println("\n---- End of " + getPollutantName(pollutant) + " Data ----");
    }

    // Example usage (fetch live data for multiple locations)
    public static void main(String[] args) {
        try {
            List<double[]> locations = UKLocationGenerator.generateUKLocations("London");

            fetchAndWriteCSV(locations, "no2", null);
            fetchAndWriteCSV(locations, "pm10", null);
            fetchAndWriteCSV(locations, "pm25", null);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}