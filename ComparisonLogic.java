import java.util.HashMap;
import java.util.List;

/**
 * This class provides the logic for comparing pollutant data between 
 * between multiple regions, years and locations
 *
 * @author Yousef Omer-Hashi
 * @version 1.0
 */
public class ComparisonLogic
{
    /**
     * @param  region1  The first region
     * @param regionName1 The name of the first region
     * @param year1 The first year of data
     * @param  region2  The seconf region
     * @param regionName2 The name of the second region
     * @param year2 The second year of data
     * @param repo The data repository containg pollution datasets
     * @return a hashmap with the pollutant as the key and a Comparison Result containing
     * both average pollution data values and units
     */
    public static HashMap<String, ComparisonResult> 
    comparePollutants(Region region1, String regionName1, 
    String year1, Region region2, String regionName2, String year2, DataRepository repo)
    {
        Statistics s1 = new Statistics(region1);
        Statistics s2 = new Statistics(region2);
        
        String[] pollutants = {"NO2", "PM10", "PM2.5"};
        HashMap <String, ComparisonResult> results = new HashMap<>();
        
        for (String pollutant: pollutants) {
            DataSet ds1 = repo.locateSet(year1, pollutant);
            DataSet ds2 = repo.locateSet(year2, pollutant);
            
            double avg1 = getAverage(s1, ds1, regionName1);
            double avg2 = getAverage(s2, ds2, regionName2);
            
            String units = (ds1 != null) ? ds1.getUnits() : "";
            results.put(pollutant, new ComparisonResult(avg1, avg2, units));
       }
       return results;
        
    }
    
    /**
     * This method calculates average pollution level for a region based on 
     * the filtered data from the DataSet.
     * @param stats The object used for calculating the data. 
     * @param ds The dataset which contains the data points.
     * @param region Region which user selects.
     * @return The average pollution value within the region selected.
     */
    
    public static double getAverage(Statistics stats, DataSet ds, String zone) {
        if (ds == null) {
            return 0.0;
        }
        List<DataPoint> mapPoints = stats.filterDataWithinZone(ds.getData());
        List<DataPoint> zonePoints = stats.filterByZone(mapPoints, zone);
        return stats.calculateAvgPollution(zonePoints);
    }
}
