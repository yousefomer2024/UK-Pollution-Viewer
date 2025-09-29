import javafx.scene.image.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
/**
 * A region holds all relevant data that constructs a region. 
 * 
 * Its dimensions are based on the British National Grid System which must
 * be known in context to the image used for the region
 *
 * @author Ali Demir
 * @version 24/03/25
 */
public class Region
{
    private String name;
    private Image image;
    private double region_right;
    private double region_left;
    private double region_bottom;
    private double region_top;
    private Canvas canvas;
    
    /**
     * Constructor for objects of class Region
     */
    public Region(String name, Image image, double region_right, double region_left, double region_bottom, double region_top)
    {
        this.name = name;
        this.image = image;
        this.region_right = region_right;
        this.region_left = region_left;
        this.region_bottom = region_bottom;
        this.region_top = region_top;
        this.canvas = createCanvas();
    }
    
    /**
    * Creates a canvas which has the graphic of the image associated with the current region
     * @return Canvas 
     */
    private Canvas createCanvas()
    {
        Canvas currentCanvas = new Canvas(image.getWidth(),image.getHeight());
        GraphicsContext mapGraphics = currentCanvas.getGraphicsContext2D();
        mapGraphics.drawImage(image,0 , 0);
        return currentCanvas;
    }
    
    /**
     * Clears the canvas from any markers by redrawing it to its initial image
     */
    public void clearCanvas()
    {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        g.drawImage(image, 0 , 0);
    }
    
    /**
     * Returns the canvas of the region
     * @return Canvas canvas 
     */
    public Canvas getCanvas()
    {
        return canvas;
    }
    
    /**
     * Returns the name of the location
     * @return String name of the region
     */
    public String getRegionName()
    {
        return name;
    }
    
    /**
     * Returns the image associated with the Region
     * @return Image image of region
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * Returns the right X coordinate boundary of the real region 
     * @return double X coordinate of right of region
     */
    public double getRegionRight()
    {
        return region_right;
    }
  
    /**
     * Returns the left X coordinate boundary of the real region 
     * @returndouble X coordinate of left of region
     */
    public double getRegionLeft()
    {
        return region_left;
    }

    /**
     * Returns the bottom Y coordinate boundary of the real region 
     * @return double Y coordinate of bottom of region
     */
    public double getRegionBottom()
    {
        return region_bottom;
    }

    /**
     * Returns the top Y coordinate boundary of the real region 
     * @return double Y coordinate of top of region
     */
    public double getRegionTop()
    {
        return region_top;
    }

    /**
     * Returns the difference between the top and the bottom
     * @return double Data height of region
     */
    public double getDataHeight()
    {
        return region_top - region_bottom;
    }
    
    /**
     * Returns the difference between the right and the left
     * @return double Data width of region 
     */
    public double getDataWidth()
    {
        return region_right - region_left;
    }

    /**
     * Returns the scalar to convert an X coordinate of the panel/screen to equivalent region X coordinate
     * @return double X scalar
     */
    public double toRegionScaleX()
    {
        return (getDataWidth()) / image.getWidth();
    }

    /**
     * Returns the scalar to convert an Y coordinate of the panel/screen to equivalent region Y coordinate
     * @return double Y scalar
     */
    public double toRegionScaleY()
    {
        //Negative to make y increase as you visually go down on the screen to mimic how Y coordinate behaves within a Region
        return -(getDataHeight()) / image.getHeight();
    }

    /**
     * Returns the scalar to convert an X coordinate of the region to equivalent image/screen X coordinate
     * @return double X scalar
     */
    public double toScreenScaleX()
    {
        return image.getWidth() / getDataWidth();
    }

    /**
     * Returns the scalar to convert an Y coordinate of the region to equivalent image/screen Y coordinate
     * @return double Y scalar
     */
    public double toScreenScaleY()
    {
        //Negative to make y increase as you visually go down on the screen to mimic how Y coordinate behaves within a Region
        return image.getHeight() / -getDataHeight();
    }
}
