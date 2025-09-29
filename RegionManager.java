import javafx.scene.image.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used by the main class to manage regions,handling changes and giving
 * GUI information 
 * 
 * @author Ali Demir
 * @version 24/03/25
 */
public class RegionManager
{
    private  HashMap<String, Region> regions;
    private ArrayList<Region> regionsList;
    /**
     * Constructor for objects of class RegionManager
     */
    public RegionManager()
    {
        regions = new HashMap<>();
        regionsList = new ArrayList<>();
        createRegions();
        createLocationStrings();
    }
    
    /**
     * Returns a region based on the name
     * @param String location name of the region
     * @return Region region associated with the location
     */
    public Region getRegion(String location)
    {
        location = location.trim().toLowerCase();
        return regions.get(location);
    }
    
    /**
     * Returns how many regions there currently are
     * @return int number of regions
     */
    public int getRegionAmount()
    {
        return regions.size();
    }
    
    /**
     * Returns a region based on an index in the array
     * @param int index number that must be greater than 0 and less than size of array
     * @return Region region at index 
     */
    public Region getRegionIndex(int index)
    {
        return regionsList.get(index);
    }

    /**
     * Creates all regions and places them in hashmap where they can be retrieved by their location name
     */
    public void createRegions()
    {
        //right left bottom top
        Region LondonRegion = new Region("London", new Image("file:London.png"),553297,510394,168504,193305);
        Region ManchesterRegion = new Region("Manchester",new Image("file:Manchester.png"),393973,363528,389216,407541);
        Region BirminghamRegion = new Region("Birmingham",new Image("file:Birmingham.png"),418380 ,387874 ,275375, 293377);
        Region NottinghamRegion = new Region("Nottingham",new Image("file:Nottingham.png"),471649, 441022,325594,343384);
        
        regions.put("london", LondonRegion);
        regions.put("manchester", ManchesterRegion);
        regions.put("birmingham", BirminghamRegion);
        regions.put("nottingham", NottinghamRegion);
    }
    
    /**
     * Fills an array with the current regions
     */
    public void createLocationStrings()
    {
        for (Region map: regions.values()){
            regionsList.add(map);
        }
    }
}
