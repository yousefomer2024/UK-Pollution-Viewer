import java.util.ArrayList;
import java.util.List;
import uk.me.jstott.jcoord.*;

/**
 * UKLocationGenerator class generates a list of UK locations based on gridcode, easting, and northing
 * values from a given CSV file, and converts them to latitude and longitude using the JCoord library.
 * The locations are filtered by a specified region.
 *
 * @author Ozgur Dorunay
 * @version 24/03/25
 */
public class UKLocationGenerator {

    // File path for the CSV data containing the pollution data
    private static final String fileName = "UKAirPollutionData/pm2.5/mappm252018g.csv";

    // RegionManager instance to handle region-based data filtering
    private static RegionManager regionManager = new RegionManager(); 

    /**
     * Generates a list of UK locations by extracting gridcode, easting, and northing
     * from the DataLoader, then converting to latitude/longitude. Only includes gridcodes 
     * within the specified region.
     *
     * @param regionName The name of the region to filter by (e.g., "London")
     * @return List of locations, where each location is represented as an array of {gridcode, easting, 
     *         northing, latitude, longitude}
     */
    public static List<double[]> generateUKLocations(String regionName) {
        // List to store the resulting locations
        List<double[]> locations = new ArrayList<>();

        // Retrieve the specified region from RegionManager
        Region region = regionManager.getRegion(regionName);
        if (region == null) {
            // Handle case where the region is not found
            System.out.println("Region not found: " + regionName);
            return locations;
        }

        // Define the boundaries of the region (left, right, bottom, top)
        double left = region.getRegionLeft();
        double right = region.getRegionRight();
        double bottom = region.getRegionBottom();
        double top = region.getRegionTop();

        // Use DataLoader to read data from the CSV file
        DataLoader loader = new DataLoader();
        DataSet dataSet = loader.loadDataFile(fileName);
        if (dataSet == null) {
            // Handle case where the data could not be loaded
            System.out.println("Failed to load data from " + fileName);
            return locations;
        }

        // Process each data point in the loaded dataset
        for (DataPoint data : dataSet.getData()) {
            try {
                // Extract gridcode, easting, and northing from the data point
                int gridcode = data.gridCode();  
                int easting = data.x();          
                int northing = data.y();         

                // Check if the data point is within the specified region boundaries
                if (isWithinRegion(easting, northing, left, right, bottom, top)) {
                    // Convert the easting and northing coordinates to latitude and longitude
                    double[] latLon = CoordinateConverter.convertToLatLon(easting, northing);

                    // Add the location to the list with all relevant data
                    locations.add(new double[]{gridcode, easting, northing, latLon[0], latLon[1]});
                }
            } catch (Exception e) {
                // Skip invalid data rows and print an error message
                System.out.println("Skipping invalid data row: " + data);
                e.printStackTrace();  
            }
        }

        // Return the generated list of locations
        return locations;
    }

    /**
     * Checks whether a given point (easting, northing) is within the specified region boundaries.
     * 
     * @param easting The easting coordinate of the point
     * @param northing The northing coordinate of the point
     * @param left The left boundary of the region
     * @param right The right boundary of the region
     * @param bottom The bottom boundary of the region
     * @param top The top boundary of the region
     * @return true if the point is within the region, false otherwise
     */
    private static boolean isWithinRegion(int easting, int northing, double left, double right, double bottom, double top) {
        // Check if the easting and northing are within the region's boundaries
        return (easting >= left && easting <= right && northing >= bottom && northing <= top);
    }

    /**
     * Main method to test the UKLocationGenerator functionality.
     * 
     * @param args Command line arguments (not used in this case)
     */
    public static void main(String[] args) {
        // Specify the region name to generate locations for
        String regionName = "London"; // Change to other regions if needed

        // Generate the list of locations for the given region
        List<double[]> locations = generateUKLocations(regionName);

        // Print the first few locations to verify the result
        System.out.println("Generated locations for region: " + regionName);
        for (int i = 0; i < Math.min(10, locations.size()); i++) {
            double[] loc = locations.get(i);
            System.out.printf("Gridcode: %d | Easting: %d | Northing: %d | Lat: %.6f | Lon: %.6f%n",
                (int) loc[0], (int) loc[1], (int) loc[2], loc[3], loc[4]);
        }
    }
}
