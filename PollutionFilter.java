import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Paint.*;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import java.util.ArrayList;
import javafx.scene.control.Label;
import java.util.List;
import java.util.HashMap;

/**
 * Applys a filter onto a canvas derived from the current map within the main program
 * using markers
 *
 * @author Ali Demir
 * @version 24/03/25
 */
public class PollutionFilter
{
    //X and Y relevant coordinates concerning image of London
    private ArrayList<Marker> markers = new ArrayList<Marker>();

    /**
     * Applies the visualisation effect onto a canvas by drawing on markers
     * @param Canvas the current canvas in the interface
     * @param Map the current map that derives the canvas
     * @param DataSet the dataset based on the year 
     */
    public void applyVisualisationEffect(Canvas currentCanvas, Region currentRegion, DataSet dataSet, HashMap filters)
    {
        //Type of data 
        String pollutant = dataSet.getPollutant();
        //Data concerned
        List<DataPoint> dataList = dataSet.getData();
        //Canvas attributes
        GraphicsContext g = currentCanvas.getGraphicsContext2D();
        g.setGlobalAlpha(0.16); //opacity
        GaussianBlur blur = new GaussianBlur(20); // Blur effect 
        g.setEffect(blur);
        
        Image currentImage = currentRegion.getImage();
        
        double map_left = currentRegion.getRegionLeft();
        double map_top = currentRegion.getRegionTop();

        double mapHeight = currentImage.getHeight();
        double mapWidth = currentImage.getWidth();
       
        //Scalars to fit data values according to dimensions of image
        
        double xScale = currentRegion.toScreenScaleX();
        double yScale = currentRegion.toScreenScaleY();
    
        String year = dataSet.getYear();

        for (DataPoint point : dataList){
            double x = (point.x() - map_left) * xScale;
            double y = (point.y() - map_top) * yScale;
            if((x >= -50 && x < mapWidth + 50) && (y >= -50 && y < mapHeight + 50)){
                addMarker(x,y,pollutant,point,g,year, filters);
            }
        }
        
        //Reset paint tool of graphicscontext    
        g.setGlobalAlpha(1.0);
        g.setGlobalBlendMode(BlendMode.SRC_OVER);
        g.setEffect(null);
    }

    /**
     * Draws markers onto the canvas 
     * @param double x the horizontal position of the marker
     * @param double y the vertical position of the marker
     * @param DataPoint the data point concerned from the dataset 
     * @param GraphicsContext g the canvas context 
     * @param String the year from the dataset
     */
    private void addMarker(double x, double y, String pollutant, DataPoint datapoint,GraphicsContext g, String year, HashMap<String,Boolean> filters)
    {
        Marker marker = new Marker(x,y,pollutant,datapoint,year);
        String color = marker.getColor().toString();
        boolean unfiltered = filters.getOrDefault(color,true);
        if(unfiltered){
            marker.draw(g);
            markers.add(marker);
        }
    }

    /**
     * Returns list of markers
     * @ArrayList list of markers 
     */
    public ArrayList<Marker> getMarkers() {
        return markers;
    }
}
