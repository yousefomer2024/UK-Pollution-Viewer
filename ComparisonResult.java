/**
 * ComparisonResult stores average pollution values for two regions for a specified pollutant
 * and the units of measurement.
 *
 * @author Yousef Omer-Hashi
 * @version 1.0
 */
public class ComparisonResult
{
    // instance variables - replace the example below with your own
    private double avg1;    //average pollution for region 1
    private double avg2;    // average pollution for region 2
    private String units;   //units of measurement for the pollutant

    /**
     * Constructor for a ComparisonResult object with the average values and units for the pollutant.
     */
    
    public ComparisonResult(double avg1, double avg2, String units)
    {
        // initialise instance variables
        this.avg1 = avg1;
        this.avg2 = avg2;
        this.units = units;
    }
    
    public double getAvg1() {
        return avg1;
    }

    public double getAvg2() {
        return avg2;
    }   

    public String getUnits() {
        return units;
    }
}
