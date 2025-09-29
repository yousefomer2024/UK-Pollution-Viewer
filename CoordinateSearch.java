import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.event.*;
import java.util.HashSet;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

/**
 * Coordinate search handles the logic and GUI interaction 
 * for the popup that allows users to search data through
 * x and y coordinates
 *
 * @author Ali Demir
 * @version 24/03/25
 */
public class CoordinateSearch extends Search
{
    protected TextField xBox;
    protected TextField yBox;
    protected Label resultLabel;

    public CoordinateSearch(DataRepository repo)
    {
        super(repo);
    }

    /**
     * Creates the content inside the popup
     * @param Pane container for the popup 
     */
    public void createPopUp(Pane parent)
    {
        VBox content = new VBox(3);
        Label inputLabel = new Label("Enter Coordinates");
        inputLabel.setId("titleLabel");
        Label xLabel  = new Label("X: ");
        this.xBox = new TextField();
        xBox.setPrefWidth(100);
        xBox.setMaxWidth(100);
        Label yLabel = new Label("Y: ");
        this.yBox = new TextField();
        yBox.setPrefWidth(100);
        yBox.setMaxWidth(100);
        this.resultLabel = new Label("Pollution value : \n           (0,0)");
        //Layout of content
        content.setPadding(new Insets(20));

        HBox box = new HBox(3,yearSelector, pollutantSelector); 
        box.setAlignment(Pos.CENTER);
        content.getChildren().addAll(inputLabel,xLabel,xBox,yLabel,yBox,box,submitButton,resultLabel);
        content.setAlignment(Pos.CENTER);
        parent.getChildren().add(content);
    }

    /**
     * Handles the interaction by checking if the given input is valid
     */
    protected void handleInput(ActionEvent event)
    {
        String xVal = xBox.getText();
        String yVal = yBox.getText();
        xBox.clear();
        yBox.clear();
        //Check if the string are made up of only numbers 
        if (isNumber(xVal) && isNumber(yVal)){
            Integer x = Integer.valueOf(xVal);
            Integer y = Integer.valueOf(yVal);
            handleSearch(x,y);
            return;
        }
        resultLabel.setText("Invalid Input");
    }

    /**
     * Handles the logic for defining the neccesary details in finding the 
     * desired dataset and data point that matches the x and y coordinates 
     * @param Integer the x coordinate
     * @param Integer the y coordinate
     */
    private void handleSearch(Integer x, Integer y)
    {
        String year = yearSelector.getValue();
        String pollutant = pollutantSelector.getValue();
        DataSet dataset = repo.locateSet(year, pollutant.toLowerCase());

        //clean x and y to find closest data point if not ending in 500
        if (x % 500 != 0){
            x = findClosestPoint(x);
        }

        if (y % 500 != 0){
            y = findClosestPoint(y);
        }

        //Find datapoint assoiciated with x and y
        for (DataPoint data : dataset.getData()){
            if (data.x() == x && data.y() == y){

                resultLabel.setText("Pollution value : " + data.value() + "\n      (" + x + "," + y + ")" );
                return;
            }
        }
        resultLabel.setText("No available data");
    }

    /**
     * Returns a number closest to the number with the last 3 digits being 500 
     * due to the structure of the dataset
     * @param Integer coordinate 
     * @int coordinate rounded to the nearest 500 
     */
    
    protected int findClosestPoint(Integer val)
    {
        return ((val/1000) * 1000) + 500;
    }

    /**
     * Returns a string assoicated with the popup
     * @return String Function of the popup
     */
    public String getString()
    {
        return "Search Data : Coordinates";
    }
}