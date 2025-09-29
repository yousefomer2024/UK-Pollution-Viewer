import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import java.util.List;
import javafx.scene.control.DialogPane;
import javafx.stage.Popup;

/**
 * Represents a pollution data point on a canvas 
 * with colour-based severity and click interaction
 * 
 * @author (Zainudin Farah)
 * @version (24/03/2025)
 */
public class Marker
{
    private double x;
    private double y;
    private double pValue;
    private String pollutant;
    private DataPoint datapoint;
    private String year;
    private static int MARKER_RADIUS = 50;
    // Lower the CLICK_RANGE, the larger the actual size of the click box is (need to rename)
    private static double CLICK_RANGE = 2.5;

    public Marker(double x, double y, String pollutant, DataPoint datapoint,String year)
    {
        this.x = x;
        this.y = y;
        this.pollutant = pollutant;
        this.datapoint = datapoint;
        this.year = year;
    }

    /**
     * Draws the pollution visual according to the pollution value
     */
    public void draw(GraphicsContext gc)
    {
       gc.setFill(getColor());
       gc.fillOval(x - MARKER_RADIUS, y - MARKER_RADIUS, MARKER_RADIUS*2, MARKER_RADIUS*2);
    }
    
    /**
     * Calculates whether the mouse click is in range of a marker 
     */
    public boolean inRange(double i, double j)
    {
        if (i >=(x+CLICK_RANGE)&& i<=(x+(MARKER_RADIUS - CLICK_RANGE))){
            if (j >(y+CLICK_RANGE)&& j<(y+(MARKER_RADIUS - CLICK_RANGE))){
            return true;
            }        
        }
        return false; 
    }
    
    // Getter methods for markers
    
    public String getInfo()
    {
        return "Pollutant: "+pollutant+"\nX: " + datapoint.x()+"\nY: "+datapoint.y()+"\nValue: "+ datapoint.value() + "\nGrid Code: "+ datapoint.gridCode() + "\nYear: " + year;
    }
    
    public double getValue()
    {
        return datapoint.value();  
    }
    
    /**
     * Gets the visual color reprenstation of the data points pollution value
     */
    public Color getColor()
    {
        double pValue = datapoint.value();
        if(pollutant.equals("no2")){
            if (pValue < 10){
                return Color.PURPLE;
            }
            else if (pValue < 15){
                return Color.CYAN;
            }
            else if(pValue < 25){
                return Color.GREEN;
            }
            else if (pValue <32){
                return Color.YELLOW;
            }
            else if (pValue <37){
                return Color.ORANGE;
            }
            else{
                return Color.RED;
            }
        }
        else if(pollutant.equals("pm2.5")){
            if(pValue < 5){
                return Color.PURPLE;
            }
            else if (pValue < 7.5){
                return Color.CYAN;
            }
            else if(pValue < 9.5){
                return Color.GREEN;
            }
            else if (pValue <10.25){
                return Color.YELLOW;
            }
            
            else if (pValue <10.9){
                return Color.ORANGE;
            }
            else{
                return Color.RED;
            }
        }
        else{
            if (pValue <10){
                return Color.PURPLE;
            }
            else if (pValue < 12.5){
                return Color.CYAN;
            }
            else if(pValue < 15){
                return Color.GREEN;
            }
            else if (pValue <17.5){
                return Color.YELLOW;
            }
            else if (pValue <19){
                return Color.ORANGE;
            }
            else{
                return Color.RED;
            }
        }
    }
}

