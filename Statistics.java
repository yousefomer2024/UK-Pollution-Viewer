import java.util.List;
import java.util.stream.Collectors;
/**
 * The Statistic class handles operations related to pollution data
 * based off of the current region which is passed in as a parameter.
 * It filters out data which is outside the boundaries and contains methods for
 * calculating the average and highest pollution data in specific regions
 * 
 * 
 *
 * @Yousef Omer-Hashi
 * @version 1.0
 */
public class Statistics
{
    // Region Boundaries
    private int easting_min;
    private int easting_max;
    private int northing_min;
    private int northing_max;

    public Statistics(Region currentRegion)
    {
        //Region boundaries
        easting_min = (int) currentRegion.getRegionLeft();
        easting_max = (int) currentRegion.getRegionRight();
        northing_min = (int) currentRegion.getRegionBottom();
        northing_max = (int) currentRegion.getRegionTop();
    }
    
    /**
     * Calculates and returns average pollution value of data points within the region.
     * 
     * 
     * @param dataPoints list of DataPoint objects containing pollution data
     * @return  average pollution value or 0 if no data points are in the region
     */
    public double calculateAvgPollution(List <DataPoint> dataPoints){
        if (dataPoints.isEmpty()) return 0;
        double total = 0;
        int count = 0; // Counts datapoints in region
        for (DataPoint point : dataPoints) {
            if(isInZone(point.x(),point.y())){
                total += point.value();
                count += 1;
        }
        }
        return total/count;
    }
    
    /**
     * Finds data point with  highest pollution value in the region.
     * 
     * @param dataPoints a list of DataPoint objects containing pollution data
     * @return the DataPoint with the highest pollution value, or null if no valid data points
     */
    public DataPoint getHighestPollution(List <DataPoint> dataPoints) {
        if (dataPoints.isEmpty()) return null; //return null if the list is empty
        DataPoint highestPoint = null;
        for (DataPoint point: dataPoints) {
            if(isInZone(point.x(),point.y())){ //check if point is inside region
                if (highestPoint == null || point.value() > highestPoint.value()) {
                    highestPoint = point; //sets this point as the highest point
                }
            }
        }
        return highestPoint;
    }
    
    /**
     * Filters data points to include points inside the region only
     * 
     * @param dataPoints a list of  the DataPoint objects containing pollution data
     * @return a list of DataPoint objects that are inside the region
     */
    public List<DataPoint> filterDataWithinZone(List<DataPoint> dataPoints) {
        return dataPoints.stream()
            .filter(point -> isInZone(point.x(), point.y()))
            .collect(Collectors.toList());
    }
    
    /**
     * filters data points by region to only include points from the specified region only
     * 
     * @param dataPoints a list of DataPoint objects containing pollution data
     * @param region the name of the region to filter
     * @return a list of DataPoint objects from the specified region
     */
    public List<DataPoint> filterByZone(List<DataPoint> dataPoints, String zone) {    
        if (zone.equals("All Zones")) {
            return dataPoints; //Returns all points in the map
        }
        
        return dataPoints.stream().filter(point -> 
        determineZone(point.x(), point.y()).equals(zone)).collect(Collectors.toList());    
    }
    
    /**
    * Checks if given (x,y) coordinate is within region boundaries/map range
    * @param easting  x-coordinate/easting
    * @param northing  y-coordinate/northing
    * @return true if coordinates are within the region, false if otherwise
    */
    public boolean isInZone(int easting, int northing) {
        return (easting >= easting_min && easting <= easting_max) &&
               (northing >= northing_min && northing <= northing_max);
    }
    
    /**
     * Determines the zone bassed on the given coordinates (easting, northing).
     * The region is divided into: Northwest, North, Northeast, South, Southwest, Southeast
     * Central, East, West
     * @param x  x-coordinate/easting
     * @param y the y-coordinate /northing
     * @return name of zone
     */
    public String determineZone(int x, int y) {
        // Check if point is outside the region bounds
        if (x < easting_min || x > easting_max || y < northing_min || y > northing_max) {
            return "Outside map range";
        }
    
        int l1_x = easting_min + (easting_max - easting_min) / 3;
        int l2_x = easting_min + 2 * (easting_max - easting_min) / 3;
        int l1_y = northing_min + (northing_max - northing_min) / 3;
        int l2_y = northing_min + 2 * (northing_max - northing_min) / 3;
    
        // Determine the region
        if (x <= l1_x && y >= l2_y) return "Northwest";
        if (x > l1_x && x <= l2_x && y >= l2_y) return "North";
        if (x > l2_x && y >= l2_y) return "Northeast";
        if (x <= l1_x && y > l1_y && y <= l2_y) return "West";
        if (x > l1_x && x <= l2_x && y > l1_y && y <= l2_y) return "Central";
        if (x > l2_x && y > l1_y && y <= l2_y) return "East";
        if (x <= l1_x && y <= l1_y) return "Southwest";
        if (x > l1_x && x <= l2_x && y <= l1_y) return "South";
        if (x > l2_x && y <= l1_y) return "Southeast";
    
        return "Outside map range"; // if the point is not in any of the other regions
    }

    
    
}
