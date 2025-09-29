import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * PollutionStatsPanel is responsible for the pollution stats panel UI.
 * It calculates average and highest pollution data by the specific 
 * pollutant, region and year.
 * It also allows users to compare pollution across 2 different locations.
 * 
 *
 * @author Yousef Omer-Hashi
 * @version 1.0
 */
public class PollutionStatsPanel extends VBox {
    private DataRepository dataRepository;
    private ComboBox<String> yearSelector;
    private ComboBox<String> pollutantSelector;
    private ComboBox<String> zoneSelector;
    private Label avgPollutionLabel;
    private Label highestPollutionLabel;
    private Label no2Label, pm10Label, pm25Label;
    private Label locationLabel;
    private Button calculateButton;
    private Statistics stats;
    private Region currentRegion;
    private RegionManager regionManager;

    public PollutionStatsPanel(DataRepository dataRepository, Region currentRegion,RegionManager regionManager) {
        this.dataRepository = dataRepository;
        this.currentRegion = currentRegion;
        this.regionManager = regionManager;
        this.stats = new Statistics(currentRegion);
        setMaxWidth(275);
        setupUI();
    }
    
    /**
     * Changes the current region and initialises the statistics object with the new region.
     * 
     * @param newRegion The new region switched to.
     */
    public void changeRegionStats(Region newRegion) {
        currentRegion = newRegion;
        this.stats = new Statistics(currentRegion);
    }

    
    /**
     * sets up the UI for the pollution stats panel (combo boxes, buttons, labels and layout
     * structure).
     * 
     */
    private void setupUI() {
        setPadding(new Insets(10)); //sets padding for the panel
        setSpacing(10); 
        

        Label caluclateTitle = new Label("Calculate Pollution");
        caluclateTitle.setId("titleLabel");

        //year selector to choose year
        yearSelector = new ComboBox<>();
        yearSelector.getItems().addAll("2018", "2019", "2020", "2021", "2022", "2023");
        yearSelector.setValue("2018");

        //pollutant selector to choose pollutant
        pollutantSelector = new ComboBox<>();
        pollutantSelector.getItems().addAll("NO2", "PM10", "PM2.5");
        pollutantSelector.setValue("NO2");
        
        //HBox for the year and pollutant selectors
        HBox topSelectorBox = new HBox(10, 
        new Label("Select Year:"), yearSelector,
        new Label("Select Pollutant:"), pollutantSelector
        );
        topSelectorBox.setPadding(new Insets(5));
        
        //zone selector to choose zone
        zoneSelector = new ComboBox<>();
        zoneSelector.getItems().addAll("All Zones","North", "West", "East", "South",
        "Northeast", "Northwest", "Southeast", "Southwest", "Central");
        zoneSelector.setValue("All Zones");
        
        //button to calculate statistics
        calculateButton = new Button("Calculate statistics");
        calculateButton.setOnAction(e -> calculatestatistics());

        
        //label to display average pollution recorded in the region
        avgPollutionLabel = new Label("Average Pollution: -"); 
        //label to display highest pollution recorded in the region
        highestPollutionLabel = new Label("Highest Pollution: -");
        //label to display location selected
        locationLabel = new Label("Location: -");
        
        //Stats Comparison Section
        Label comparisonTitle = new Label("Compare Pollution Between \nTwo Locations");
        comparisonTitle.setId("titleLabel");

        //list of years, zones, and regions to compare
        List<String> years = List.of("2018", "2019", "2020", "2021", "2022", "2023");
        List<String> zones = List.of("All Zones", "North", "West", "East", "South",
        "Northeast", "Northwest", "Southeast", "Southwest", "Central");
        
        List<String> regions = List.of("London", "Manchester", "Birmingham", "Nottingham");
        
        //Location 1 UI
        Label location1Label = new Label("Location 1");
        ComboBox<String> regionSelector1 = new ComboBox<>();
        ComboBox<String> zoneComparisonSelector1 = new ComboBox<>();
        ComboBox<String> yearComparisonSelector1 = new ComboBox<>();
        regionSelector1.getItems().addAll(regions);
        regionSelector1.setValue("London");
        zoneComparisonSelector1.getItems().addAll(zones); 
        zoneComparisonSelector1.setValue("All Zones");
        yearComparisonSelector1.getItems().addAll(years); 
        yearComparisonSelector1.setValue("2023");
        Label no2Label1 = new Label("NO2: -");
        Label pm10Label1 = new Label("PM10: -");
        Label pm25Label1 = new Label("PM2.5: -");
        
        //VBox for Location 1
        VBox location1VBox = new VBox(5, location1Label,
        new Label("Region:"), regionSelector1,
        new Label("Zone:"), zoneComparisonSelector1,
        new Label("Year:"), yearComparisonSelector1,
        no2Label1, pm10Label1, pm25Label1);
        location1VBox.setPrefWidth(125);
        
        
        //Location 2 UI
        Label location2Label = new Label("Location 2");
        ComboBox<String> regionSelector2 = new ComboBox<>();
        ComboBox<String> zoneComparisonSelector2 = new ComboBox<>();
        ComboBox<String> yearComparisonSelector2 = new ComboBox<>();        
        regionSelector2.getItems().addAll(regions);
        regionSelector2.setValue("Manchester");
        zoneComparisonSelector2.getItems().addAll(zones); 
        zoneComparisonSelector2.setValue("All Zones");
        yearComparisonSelector2.getItems().addAll(years); 
        yearComparisonSelector2.setValue("2023");
        Label no2Label2 = new Label("NO2: -");
        Label pm10Label2 = new Label("PM10: -");
        Label pm25Label2 = new Label("PM2.5: -");
        
        //VBox for Location 2
        VBox location2VBox = new VBox(5, location2Label,
        new Label("Region:"), regionSelector2,
        new Label("Zone:"), zoneComparisonSelector2,
        new Label("Year:"), yearComparisonSelector2,
        no2Label2, pm10Label2, pm25Label2);
        location2VBox.setPrefWidth(125);
        
        // HBox for displaying the two locations next to each other
        HBox comparisonColumns = new HBox(20, location1VBox, location2VBox);
        comparisonColumns.setPadding(new Insets(10));
        
        Button compareButton = new Button("Compare");
        
        //compare pollution data between locations
        compareButton.setOnAction(e -> {
            //retrieve zone and year from the comparison UI elements
            String zone1 = zoneComparisonSelector1.getValue();
            String year1 = yearComparisonSelector1.getValue();
            
            String zone2 = zoneComparisonSelector2.getValue();
            String year2 = yearComparisonSelector2.getValue();
            
            //retrieve regions based on selected region names
            Region region1 = regionManager.getRegion(regionSelector1.getValue());
            Region region2 = regionManager.getRegion(regionSelector2.getValue());
        
            //compare pollutants between 2 locations
            HashMap<String, ComparisonResult> results = ComparisonLogic.comparePollutants(
            region1, zone1, year1, region2, zone2, year2, dataRepository);
            
            //retrieve comparison result for each pollutant
            ComparisonResult no2Result = results.get("NO2");
            ComparisonResult pm10Result = results.get("PM10");
            ComparisonResult pm25Result = results.get("PM2.5");
            
            //update labels with the comparison result and color coding
            updateLabelWithColor(no2Label1, no2Label2, no2Result, "NO2");
            updateLabelWithColor(pm10Label1, pm10Label2, pm10Result, "PM10");
            updateLabelWithColor(pm25Label1, pm25Label2, pm25Result, "PM2.5");
        });
        
        // create a Vbox to include all elements of the comparison section
        VBox comparisonSection = new VBox(10, comparisonTitle, comparisonColumns, compareButton);
        comparisonSection.setPadding(new Insets(10));
        comparisonSection.setMaxWidth(300);
        comparisonSection.setTranslateX(-13);
        
        //add all UI elements to the stats panel
        getChildren().addAll(caluclateTitle,new Label("Select Year:"), yearSelector,
                             new Label("Select Pollutant:"), pollutantSelector, 
                             new Label("Select Zone"), zoneSelector,
                             calculateButton, avgPollutionLabel, 
                             highestPollutionLabel, locationLabel, comparisonSection);  
    
    }
    
    /**
     * Updates comparison labels with values and applies color coding.
     * 
     * @param label1  label for location 1.
     * @param label2  label for location 2
     * @param result  comparison result for the given pollutant
     * @param pollutant name of the pollutant being compared
     */
    private void updateLabelWithColor(Label label1, Label label2, ComparisonResult result, String pollutant) {
        String formatted1 = String.format("%s: %.1f %s", pollutant, result.getAvg1(), result.getUnits());
        String formatted2 = String.format("%s: %.1f %s", pollutant, result.getAvg2(), result.getUnits());
    
        label1.setText(formatted1);
        label2.setText(formatted2);
    
        //applies color based on which value is higher
        //green for lower pollution
        //red for higher pollution
        if (result.getAvg1() < result.getAvg2()) {
            label1.setStyle("-fx-text-fill: green;");
            label2.setStyle("-fx-text-fill: red;");
        } else if (result.getAvg1() > result.getAvg2()) {
            label1.setStyle("-fx-text-fill: red;");
            label2.setStyle("-fx-text-fill: green;");
        } else {
            label1.setStyle("-fx-text-fill: grey;");
            label2.setStyle("-fx-text-fill: grey;");
        }
    }

    /**
     * Displays the statistics for selected pollutant
     * in the selected region and year by filtering points outside of the region.
     * The actual calculation logic is delegated to the selected class
     * 
     */
    private void calculatestatistics() {
        String year = yearSelector.getValue();
        String pollutant = pollutantSelector.getValue();
        
        DataSet dataSet = dataRepository.locateSet(year, pollutant);
        if (dataSet == null) {
            avgPollutionLabel.setText("No data available for selected year and pollutant");
            highestPollutionLabel.setText("");
            return;
        }


        //Filters out data points within zone boundaries
        List<DataPoint> dataPoints = stats.filterDataWithinZone(dataSet.getData());
        
        //Filter data points by the selected zone
        String selectedZone = zoneSelector.getValue();
        dataPoints = stats.filterByZone(dataPoints, selectedZone);
        
        if (dataPoints.isEmpty()) {
            avgPollutionLabel.setText("No data points found within " + currentRegion.getRegionName());
            highestPollutionLabel.setText("");
            return;
        }

        double total = 0;
        
        DataPoint highestPoint = null;


        double avgPollution = stats.calculateAvgPollution(dataPoints);
        highestPoint = stats.getHighestPollution(dataPoints);
        
        avgPollutionLabel.setText(String.format("Average Pollution: \n %.2f %s", avgPollution, dataSet.getUnits()));

        if (highestPoint != null) {
            highestPollutionLabel.setText(String.format("Highest Pollution: \n %.2f at Grid (%d, %d) ",
                                                        highestPoint.value(), highestPoint.x(), highestPoint.y()));
        }
        
        if (!selectedZone.equals("All Zones")) {
            locationLabel.setText(String.format("Location: %s %s", selectedZone, currentRegion.getRegionName()));
        } 
        else {
            locationLabel.setText(String.format("Location: %s", currentRegion.getRegionName()));
        }
    }
}
