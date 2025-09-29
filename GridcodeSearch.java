import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

/**
 * Gridcode search handles the logic and GUI interaction 
 * for the popup that allows users to search data through
 * a gridcode
 * 
 * @author Ali Demir
 * @version 24/03/25
 */
public class GridcodeSearch extends Search
{  
    private TextField gridcodeBox;
    private Label resultLabel;
    public GridcodeSearch(DataRepository repo)
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
        Label gridLabel = new Label("Enter Gridcode");
        gridLabel.setId("titleLabel");
        this.gridcodeBox = new TextField();
        gridcodeBox.setPrefWidth(100);
        gridcodeBox.setMaxWidth(100);

        HBox box = new HBox(10,yearSelector, pollutantSelector);
        this.resultLabel = new Label("Pollution Value : ");
        //Layout
        box.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.getChildren().addAll(gridLabel,gridcodeBox,box, submitButton, resultLabel);
        content.setAlignment(Pos.CENTER);
        parent.getChildren().add(content);
    }
    
    /**
     * Handles the interaction by checking if the given input is valid
     */
    protected void handleInput(ActionEvent event)
    {
        String gridcode = gridcodeBox.getText();
        if (isNumber(gridcode)){
            handleSearch(gridcode);
        }
        gridcodeBox.clear();
    }

    /**
     * Handles the logic for defining the neccesary details in finding the 
     * desired dataset and data point that matches the gridcode 
     * @param String the gridcode
     */
    private void handleSearch(String gridcode){
        int gridcodeVal = Integer.valueOf(gridcode);
        //Gets strings entered in the text areas
        String year = yearSelector.getValue();
        String pollutant = pollutantSelector.getValue();
        DataSet dataset = repo.locateSet(year, pollutant.toLowerCase());
        
        if (!checkBound(gridcodeVal)){
            resultLabel.setText("Out of bounds");
        }
        
        for (DataPoint data : dataset.getData()){
            if (data.gridCode() == gridcodeVal){
                resultLabel.setText("Pollution Value : " + data.value());
                return;
            }
        }
        resultLabel.setText("Invalid Input");
    }

    /**
     * Checks to see if the entered gridcode lies within the range
     * inside the given csv files
     * @param int the gridcode number
     * @return boolean true or false
     */
    private boolean checkBound(int gridcode)
    {
        //Hardcodes values from csv files
        if (gridcode <55671 || gridcode > 892955){
            return false;
        }
        return true;
    }
    
    /**
     *  Returns a string assoicatedw it with the popup
     *  @return String Function assoicated with the popup;
     */
    public String getString()
    {
        return "Search Data : Gridcode";
    }
}
