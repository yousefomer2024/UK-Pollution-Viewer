import javafx.scene.Scene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import javafx.stage.Modality;
import java.util.List;

/**
 * Creates popups over the main window depending on certain parameters 
 * to allow users to interact further and gain more specific data 
 *
 * @author Ali Demir
 * @version 24/03/25
 */
public class InputPopUp 
{
    private Stage stage;
    private Scene scene;
    private VBox root;
    private DataRepository repo;
    private HashMap<String , Search> searchTypes;
    /**
     * Constructor for objects of class InputPopUp
     */
    public InputPopUp(DataRepository repo)
    {
        this.repo = repo;
        this.searchTypes = new HashMap<>();
        createSearchTypes();
    }

    /**
     * Lay the foundation for the window that the popup will exist inside
     * @param String type of popup 
     * @param Stage ownerstage of the main window 
     * @paramm List<String> current style of the main window 
     */
    public void setUpPopup(String type, Stage ownerStage, List<String> styles)
    {
        type = type.toLowerCase();
        Search searchType = searchTypes.get(type);
        if (searchType != null){
            //New window 
            Stage popupStage = new Stage();
            popupStage.initOwner(ownerStage); //displayed on top of the main window
            popupStage.initModality(Modality.APPLICATION_MODAL); //cannot access main window until popup is closed 
            popupStage.setTitle(searchType.getString()); //title 

            VBox popupRoot = new VBox();
            popupRoot.setId("searchbox");
            searchType.createPopUp(popupRoot);
            Scene popupScene = new Scene(popupRoot);
            popupScene.getStylesheets().addAll(styles);
            
            popupStage.setScene(popupScene);
            
            popupStage.show();
        }
        else{
            return;
        }
    }   

    /**
     * Initialise the search types with the current repository 
     */
    private void createSearchTypes()
    {
        CoordinateSearch coordSearch = new CoordinateSearch(repo);
        GridcodeSearch gridSearch = new GridcodeSearch(repo);
        
        searchTypes.put("coordinates", coordSearch);
        searchTypes.put("gridcode",gridSearch);
    }
}