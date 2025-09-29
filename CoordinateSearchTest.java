import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class CoordinateSearchTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class CoordinateSearchTest
{
    private DataRepository dataRepo1;

    /**
     * Default constructor for test class CoordinateSearchTest
     */
    public CoordinateSearchTest()
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
        dataRepo1 = new DataRepository();
        
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

    @Test
    public void testValidString()
    {
        CoordinateSearch coordina1 = new CoordinateSearch(dataRepo1);
        assertEquals(true , coordina1.isNumber("1619500"));
        assertEquals(false, coordina1.isNumber("29a35"));
    }


    @Test
    public void validRounding()
    {
        CoordinateSearch coordina1 = new CoordinateSearch(dataRepo1);
        assertEquals(4619500, coordina1.findClosestPoint(4619400));
        assertEquals(461500, coordina1.findClosestPoint(461900));
    }
}


