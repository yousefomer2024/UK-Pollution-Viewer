import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.layout.Pane; 
import java.util.List;
import javafx.stage.Popup;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tab;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import javafx.scene.input.ScrollEvent;
import java.text.DecimalFormat;

/**
 * Creates a pollution data graph, allowing users to 
 * view and interact with pollution trends.
 *
 * @author Zainudin Farah
 * @version 24/03/2025
 */
public class GraphTab extends Tab
{
    private Statistics stats;
    private DataRepository repo;
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private final AreaChart<Number,Number> ac;
    private Stage stage;
    
    /** 
     * Constructor of the Tab Subclass, Graph Tab
     * Created to contain and the pollution trend graph
     */
    public GraphTab (DataRepository repo, Stage stage)
    {
        super("Graph");
        this.repo = repo;
        this.xAxis = new NumberAxis(2018, 2023, 1);
        this.yAxis = new NumberAxis();
        this.ac = new AreaChart<Number,Number>(xAxis,yAxis);
        this.stage = stage;
        ac.setId("graph");
        setContent(ac);
    } 
    
    public void plotGraph(Region currentRegion, String zone, String trend){
        ac.getData().clear();
        this.stats = new Statistics(currentRegion);
        
        if(trend.equals("Average"))
        ac.setTitle("Average Pollution Level (g/m^3)");
      
        if(trend.equals("Highest"))
        ac.setTitle("Highest Pollution Level (g/m^3)");
        
        // Plots lines for all three pollutants 
        createLine("no2",currentRegion, zone, trend);
        createLine("pm10",currentRegion, zone,trend);
        createLine("pm2.5",currentRegion, zone,trend);
        
        
        // Adjusts Y-Axis when graph data changes
        ac.getYAxis().setAutoRanging(true);
        ac.requestLayout(); 
        ac.layout(); 
        // Sets sizes for graph
        ac.setPrefSize(800, 600); 
        ac.setMinSize(400, 300); 
        ac.setMaxSize(1600, 1200); 
    }
    
    /**
     * Creates a graph line based on pollution, region, zone and trend
     */
    public void createLine(String pollutant, Region currentRegion, String zone, String trend)
    {
      XYChart.Series series = new XYChart.Series();   
      series.setName(pollutant);
      double pValue = 0.0;
      
      for(int i = 2018; i < 2024; i++){
        // Fetches data that will be used to calculate values based on trend and 
        List<DataPoint> data = repo.locateSet(Integer.toString(i),pollutant).getData();
        List<DataPoint> zoneData = stats.filterByZone(data,zone);
        
        if(trend.equals("Average")) {
            pValue = stats.calculateAvgPollution(zoneData);
        }
        
        if(trend.equals("Highest")) {
            pValue = stats.getHighestPollution(zoneData).value();
        }
        XYChart.Data point = createPoint(i,pValue,trend);
        series.getData().add(point);
        }
        
      ac.getData().add(series);
    }    
    
 
    
     /**
      * Creates interactive graph point based on inputted data
      */
   
    public XYChart.Data createPoint(int year, double pValue, String trend)
    {
      XYChart.Data point =  new XYChart.Data(year,pValue);
      
      //Creates the clickable shape at point
      Circle circle = new Circle(4);
      circle.setStroke(Color.GREY);
      circle.setFill(Color.WHITE); 
      point.setNode(circle);
      
      point.getNode().setOnMouseClicked(event ->{ 
      double x = event.getSceneX(); 
      double y = event.getSceneY();   

      Popup markerPopUp = createPopUp(x, y, year, pValue, trend);
      //Hide popup when clicking outside of marker
      stage.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->{
                        if (markerPopUp.isShowing() && !markerPopUp.getContent().contains(event.getTarget())){
                            markerPopUp.hide();
                       }});
      });
      return point;
    }
   
    /**
     * Creates pop-up that displays a point's value
     */
    
    private Popup createPopUp(double x, double y, int year, double pValue, String trend)
    {
        VBox holder = new VBox();
        holder.setId("dataBox");
        Popup markerPopUp = new Popup();
        Label markerData = new Label();
        
        DecimalFormat df = new DecimalFormat("#.0000");
        String pString = df.format(pValue);
        
        if(trend.equals("Average"))
         markerData.setText("Year: "+ year + "\nAverage Pollution: " + pString);
        
        if(trend.equals("Highest"))
         markerData.setText("Year: "+ year + "\nHighest Pollution: " + pString); 
        
        markerData.setId("markerText");
        holder.getChildren().add(markerData);
        markerPopUp.getContent().add(holder);
        markerPopUp.setAnchorX(x + 5);
        markerPopUp.setAnchorY(y + 5);
        markerPopUp.show(stage);
        return markerPopUp;
    }
}
