import java.util.HashMap;
import java.util.List;

/**
 * Handles all datasets used within the program
 * @author Ali Demir and Ozgur Dorunay
 * @version 24/03/25
 */
public class DataRepository
{
    private HashMap<String, DataSet> no2Sets;
    private HashMap<String, DataSet> pm10Sets;
    private HashMap<String, DataSet> pm25Sets;
    
    /**
     * Constructor for objects of class dataRepository
     */
    public DataRepository()
    {
        no2Sets = new HashMap<>();
        pm10Sets = new HashMap<>();
        pm25Sets = new HashMap<>();
        createDataSets();
    }
    
    /**
     * Locates a dataset from the year and type
     * @param String year the year where data is concerned
     * @param String type1 the type of data 
     * @return DataSet the dataset concerned 
     */
    public DataSet locateSet(String year , String type1)
    {
        String type = type1.toLowerCase().trim();
        if (type.equals("no2")){
            return no2Sets.get(year);
        }
        else if(type.equals("pm10")){
            return pm10Sets.get(year);
        }
        else if(type.equals("pm2.5")){
            return pm25Sets.get(year);
        }
        else{
            return null;
        }
    }
    
    /**
     * Creates dataSets from csv files using data loader and puts them in a hashmap accesible by their corresponding year 
     */
    private void createDataSets()
    {
        DataLoader loader = new DataLoader();
        
        for (int i = 0; i < 6; i++){
            DataSet no2Set = loader.loadDataFile("UKAirPollutionData/NO2/mapno220" + (18 + i) +  ".csv");
            no2Sets.put("" + (2018 + i) + "", no2Set);
        }
        
        for (int i = 0; i<6 ; i++){
            DataSet pm25Set = loader.loadDataFile("UKAirPollutionData/pm2.5/mappm2520" + (18 + i) + "g.csv");
            pm25Sets.put("" + (2018 + i) + "", pm25Set);
        }
        
        for(int i = 0; i<6;i++){
            DataSet pm10Set = loader.loadDataFile("UKAirPollutionData/pm10/mappm1020" + (18 + i) + "g.csv");
            pm10Sets.put("" + (2018 + i) + "", pm10Set);
        }
    }
    
     /**
     * Loads live data from the CSV file into the repository
     * @param type The type of pollution data (no2, pm10, pm2.5)
     */
    public void loadLiveData(String type) {
        DataLoader loader = new DataLoader();
        DataSet liveDataSet;

        // The live data will be saved to a specific filename by LiveDataCSVWriter
        String filename = "UKAirPollutionData/" + type + "/map" + type + "live.csv";
        liveDataSet = loader.loadDataFile(filename);

        // Add the dataset to the appropriate map with "live" as the key
        if (type.equals("no2")) {
            no2Sets.put("live", liveDataSet);
        } else if (type.equals("pm10")) {
            pm10Sets.put("live", liveDataSet);
        } else if (type.equals("pm2.5")) {
            pm25Sets.put("live", liveDataSet);
        }
    }
}
