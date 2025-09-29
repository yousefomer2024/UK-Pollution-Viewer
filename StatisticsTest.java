import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import javafx.scene.image.*;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * The test class StatisticsTest contains unit test for the 
 * Statistics class.
 * 
 *
 * @author  Yousef Omer-Hashi
 * @version 1.0
 */
public class StatisticsTest
{
    private Statistics stats;
    private Region testRegion;
    private WritableImage testImage;
    /**
     * Default constructor for test class StatisticsTest
     */
    public StatisticsTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        //initialises a test image
        testImage = new WritableImage(1,1);
        //initialises a test region to use in all test cases with easting
        //and northing boundaries of 250
        testRegion = new Region("TestRegion",testImage, 250, 0, 0, 250);
        //initialises stats object with test region
        stats = new Statistics(testRegion);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
    
    /**
     * This method tests the calculation of average pollution of a list of data points
     * is correct.
     * 
     */
    @Test
    public void testCalculateAvgPollution() {
        List<DataPoint> points = List.of(
            new DataPoint(1, 100, 100, 60.0),  // inside
            new DataPoint(2, 150, 250, 40.0), // inside
            new DataPoint(3, 251, 100, 75.0), //outside
            new DataPoint(4, 0, 0, 0.0), //inside
            new DataPoint(5, 200, 20, 50.0) //inside
        );
        // Check that calculated average pollution is correct 
        // and only includes data points in the region boundaries.
        assertEquals(37.5, stats.calculateAvgPollution(points), 0.001);
    }
    

    @Test
    /**
     * This method tests the calculation of average pollution of a empty list
     * is given as 0.0.
     * 
     */
    public void testCalculateAvgPollutionEmptyList() {
        List<DataPoint> points = List.of();
        //Check that average pollution is 0.0 for empty list
        assertEquals(0.0, stats.calculateAvgPollution(points), 0.001);
    }

    
    /**
     * Test method for calculating the highest pollution from a list of data points.
     */
    @Test
    public void testGetHighestPollution() {
        List<DataPoint> points = List.of(
            new DataPoint(1, 50, 50, 50.0), //inside
            new DataPoint(2, 200, 200, 30.0), //inside
            new DataPoint(3, 251, 100, 60.0), //outside of boundary
            new DataPoint(4, -5, -5, 100.0), //outside
            new DataPoint(5, 250, 250, 70.0) //inside
        );

        DataPoint highest = stats.getHighestPollution(points);
        assertEquals(70.0, highest.value(), 0.001); //check highest pollution value is correct
        assertEquals(5, highest.gridCode()); //check grid code is correct
    }

    /**
     * Test method for retrieving the highest pollution data point from an empty list.
     */
    @Test
    public void testGetHighestPollutionEmptyList() {
        List<DataPoint> points = List.of();
        DataPoint highest = stats.getHighestPollution(points);
        // ensure that null is returned for an empty list
        assertNull(highest);
    }

    /**
     * Tests if various given points is within region boundaries.
     */
    @Test
    public void testIsInRegionBoundary() {
        assertTrue(stats.isInZone(0, 0));           // inside boundary
        assertTrue(stats.isInZone(10, 10));       // inside boundary
        assertTrue(stats.isInZone(250, 250));       // Inside region
        assertFalse(stats.isInZone(-5, 0));         // outside left of boundary
        assertFalse(stats.isInZone(251, 240));      // Outside right of boundary
        assertFalse(stats.isInZone(240, 300));      // outside top of boundary
        assertFalse(stats.isInZone(0, -5));           //outside bottom of boundary
    }
    
    /**
     * Test method for correctly filtering data points that fall inside and outside 
     * the zone's boundaries.
     */
    @Test
    public void testFilterDataWithinZone() {
        List<DataPoint> points = List.of(
            new DataPoint(1, 10, 50, 4.0),          //inside region
            new DataPoint(2, 250, 250, 8.0),        // inside region
            new DataPoint(3, 100, 15, 6.0),         // inside region
            new DataPoint(4, 251, 251, 9.0),        //outside region
            new DataPoint(5, 300, 200, 15.0),       //outside region
            new DataPoint(6, 0, 0, 10.0)            //inside region       
        );
    
        List<DataPoint> filtered = stats.filterDataWithinZone(points);
        assertEquals(4, filtered.size());
        assertEquals(1, filtered.get(0).gridCode());
        assertEquals(2, filtered.get(1).gridCode());
        assertEquals(3, filtered.get(2).gridCode());
        assertEquals(6, filtered.get(3).gridCode()); 
    }
    
    /**
     * Test method for determining region names based on the coordinates of different point.
     */
    @Test
    public void testDetermineRegionCorners() {
        // Region boundaries: 0 to 250
        assertEquals("Southwest", stats.determineZone(0, 0));
        assertEquals("West", stats.determineZone(0, 125));
        assertEquals("Northwest", stats.determineZone(0, 250));
        assertEquals("Northeast", stats.determineZone(250, 250));
        assertEquals("Central", stats.determineZone(125, 125));
        assertEquals("Outside map range", stats.determineZone(251, 251));
        assertEquals("East", stats.determineZone(250, 125));
    }
}
