import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import java.util.*;

/**
 * Search lays the foundations for a pop up orientated around searching 
 * data given a certain type of input
 *
 * @author Ali Demir
 * @version 24/03/25
 */
abstract public class Search
{
    protected Button submitButton;
    protected DataRepository repo;
    protected ComboBox<String> yearSelector;
    protected ComboBox<String> pollutantSelector;
    /**
     * Constructor for objects of class searchType
     */
    public Search(DataRepository repo)
    {
        this.repo = repo;
        this.submitButton = new Button("Submit");
        submitButton.setOnAction(this::handleInput);
        submitButton.setId("submitButton");
        createComboBoxes();
    }
    
    /**
     * Create the content inside the parent to display a popup 
     * @param Pane parent container
     */
    abstract public void createPopUp(Pane parent);
    
    /**
     * Handle the input behind the submit button 
     */
    abstract protected void handleInput(ActionEvent event);
    
    /**
     * Return a string assoicated with the title information of the popup
     * @return String title of the popup 
     */
    abstract public String getString();
    
    /**
     * Checks if a string is only numbers
     * @param String value inside string 
     * @return boolean true or false
     */
    protected boolean isNumber(String value)
    {
        HashSet<Character> numbers = new HashSet<>();
        for (char c = '0'; c <= '9'; c++) {
            numbers.add(c);
        }
        for (Character ch : value.toCharArray()){
            if (!numbers.contains(ch)){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Creates combo boxes allowing the user to select a preffered
     * year and type 
     */
    private void createComboBoxes()
    {
        this.yearSelector = new ComboBox<>();
        yearSelector.getItems().addAll("2018", "2019", "2020", "2021", "2022", "2023");
        yearSelector.setValue("2018");

        this.pollutantSelector = new ComboBox<>();
        pollutantSelector.getItems().addAll("NO2", "PM10", "PM2.5");
        pollutantSelector.setValue("NO2");
    }
}
