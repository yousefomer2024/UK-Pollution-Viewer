import javafx.scene.image.*;
import javafx.scene.canvas.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane.*;
import java.util.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.chart.AreaChart;

/**
 * Write a description of class Main here.
 *
 * @author Ali Demir and Zainudin Farah
 * @version 24/03/25
 */
public class Main extends Application
{
    //Current state of variables
    private Region currentRegion;
    private Canvas currentCanvas;
    private String currentYear;
    private String currentType;
    private String currentZone;
    private String currentGraph;
    
    //GUI components    
    private Stage stage;
    private Scene scene;
    private TabPane tabBar;
    private Tab mapTab;
    private ScrollPane scrollPane;
    private Label mouseCoordLabel;
    
    private boolean visualApplied;
    private ArrayList<Marker> markers;
    private HashMap<String, Boolean> colFilters;

    private DataRepository dataRepo;
    private PollutionStatsPanel statsPanel;
    private RegionManager regionManager; 
    private GraphTab graphTab; 
    
    public Main()
    {
        this.regionManager = new RegionManager();
        this.currentRegion = regionManager.getRegion("London"); //initial region -> london 
        this.currentCanvas = currentRegion.getCanvas();
        this.currentYear = "2023";
        this.currentType = "no2";
        this.currentZone = "All Zones";
        this.currentGraph = "Average";
        this.visualApplied = false;
        this.dataRepo = new DataRepository();
        this.colFilters = new HashMap<String,Boolean>(Map.of(
             "0x800080ff", true,  // Purple
             "0x00ffffff", true,  // Cyan
             "0x008000ff", true,  // Green
             "0xffff00ff", true,  // Yellow
             "0xffa500ff", true,  // Orange
             "0xff0000ff", true   // Red
        ));
    }
    
    /**
     * Applies the heat map to the canvas 
     * @param String year for the selected data
     * @param String type for the selected data
     */
    private void applyVisualise(String year, String type)
    {
        if(visualApplied){
            currentRegion.clearCanvas();
            visualApplied = false;
        }
        currentYear = year;
        currentType = type; 
        
        PollutionFilter pollutionFilter = new PollutionFilter();
        DataSet dataSet = dataRepo.locateSet(year, type);
        pollutionFilter.applyVisualisationEffect(currentRegion.getCanvas(), currentRegion, dataSet, colFilters);
        markers = pollutionFilter.getMarkers();
        visualApplied = true;
    }
    
    /**
     * Clears the map of the pollution visualisation effect
     */
    private void clearMap(ActionEvent event)
    {
        if(visualApplied){
            currentRegion.clearCanvas();
            visualApplied = false;
        }
    }
    
    /**
     * Handles the visualise click which forces the tab to the map tab and
     * calls the heatmap method
     * @param MenuItem menu item which was clicked containing the type
     * @param String year for the selected data
     */
    private void handleVisualiseClick(MenuItem item ,String year)
    {
        Menu parentMenu = (Menu) item.getParentMenu();
        String type = parentMenu.getText();
        //When visualise is called, tab pane will be on the map pane
        tabBar.getSelectionModel().selectFirst();
        applyVisualise(year, type);
    }
    
    /**
     * Handles the change of location from the menu Location 
     * @String name of the location 
     */
    private void handleLocationChange(String location)
    {
        Region newRegion = regionManager.getRegion(location);
        if (newRegion == currentRegion){
            return;
        }
        visualApplied = false;
        currentRegion.clearCanvas();
        currentRegion = newRegion;
        statsPanel.changeRegionStats(currentRegion);
        currentCanvas = currentRegion.getCanvas();
        graphTab.plotGraph(currentRegion, currentZone, currentGraph);
        scrollPane.setContent(currentCanvas);
        handleUpdate();
        handleClick();
    }

    /**
     * Handles the mouse click onto the canvas which generates a popup containing 
     * marker information
     */
    private void handleClick()
    {
        currentCanvas.setOnMouseClicked(e -> {
          if(visualApplied){
            double x = e.getSceneX(); 
            double y = e.getSceneY();
            Point2D point = currentCanvas.sceneToLocal(x,y);
            //Get the x and y relative to the image within the scroll pane
            double adjustedX = point.getX();
            double adjustedY = point.getY();
            for(Marker marker: markers){
                if(marker.inRange(adjustedX, adjustedY)){
                    Popup markerPopUp = createDataPopUp(marker,x,y);
                    //Hide popup when clicking outside of marker
                    stage.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->{
                        if (markerPopUp.isShowing() && !markerPopUp.getContent().contains(event.getTarget())){
                            markerPopUp.hide();
                    }});
                    //Hide popup when scrolling     
                    stage.addEventFilter(ScrollEvent.SCROLL, event ->{
                        if (markerPopUp.isShowing()){
                            markerPopUp.hide();
                    }});
                    return;
                }
            }
        }
      });
    }
    
    /**
     * Handles the constant update of the mouse hovering over the canvas, giving 
     * live updates of the x and y coordinates calculated from the canvas to the
     * region and pollution data when hovered over markers 
     */
    private void handleUpdate()
    {
       currentCanvas.setOnMouseMoved(event -> {
            
            double x = event.getSceneX();
            double y = event.getSceneY();

            Point2D point = currentCanvas.sceneToLocal(x,y);
            //Get the x and y relative to the image within the scroll pane
            double adjustedX = point.getX();
            double adjustedY = point.getY();
            
            // To find pollution level at location
            String pValue = findPollutionValue(adjustedX, adjustedY);

            double x1 = (adjustedX * currentRegion.toRegionScaleX()) + currentRegion.getRegionLeft();
            double y1 = (adjustedY * currentRegion.toRegionScaleY()) + currentRegion.getRegionTop();
            
            mouseCoordLabel.setText(" X : " + (int) x1 + " Y : " + (int) y1 + "\n Pollution Level: " + pValue);
            });
    
       currentCanvas.setOnMouseExited(event ->{
            mouseCoordLabel.setText(" X : " + (int) 0 + " Y : " + (int) 0 + "\n Pollution Level: "+ 0);
            });      

    }
    
    /**
     * Handles the creation of popups which are accustom to the selected
     * input and follow the same style of the existing window
     */
    private void handlePopup(MenuItem item)
    {
        String type = item.getText();
        InputPopUp searchPopUp = new InputPopUp(dataRepo);
        List<String> styles = scene.getStylesheets();
        searchPopUp.setUpPopup(type,stage,styles);
    }
    
    /**
     * Shows welcome/instructions window
     */
    private void showInstructions()
    {
        WelcomeWindow welcome = new WelcomeWindow(stage);
        welcome.showAndWait();
    }
    
    @Override
    public void start(Stage stage)
    {
        this.stage = stage;
        
        // Show the Welcome Window first
        showInstructions(); // Blocks execution until "Finish" is clicked
        
        // After Welcome interaction is compeleted, start the main program
        VBox root = new VBox();
        makeMenuBar(root);
        
        statsPanel = new PollutionStatsPanel(dataRepo, currentRegion, regionManager);
        
        Pane boxPanel = new VBox();
        makeCheckBox(boxPanel);
        makePlotBox(boxPanel);
        
        //Tab bar for statistic and filter components
        TabPane toolBar = new TabPane();  
        Tab filterTab = new Tab("Filters", boxPanel);
        Tab statsTab = new Tab("Statistics", statsPanel);
        statsTab.setClosable(false);
        filterTab.setClosable(false);
        toolBar.getTabs().addAll(statsTab,filterTab);
        toolBar.setId("tabbar");
        
        //Tabbar for users to switch between map and graph
        tabBar = new TabPane();
        tabBar.setId("tabbar");

        this.graphTab = new GraphTab(dataRepo,stage);
        graphTab.plotGraph(currentRegion,"All Zones","Average");

        this.scrollPane = new ScrollPane();
        scrollPane.setContent(currentRegion.getCanvas());
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setId("ImageScroller");
        
        this.mapTab = new Tab("Map",scrollPane);
        mapTab.setClosable(false);
        graphTab.setClosable(false);
        tabBar.getTabs().addAll(mapTab,graphTab);
        
        //Label to display live x, y and pollution value updates from the map
        VBox textSpace = new VBox();
        this.mouseCoordLabel = new Label("  X : 0  Y : 0 \n");
        mouseCoordLabel.setMinHeight(10);
        textSpace.setId("coordLabel");
        textSpace.getChildren().add(mouseCoordLabel);
        //Layout
        BorderPane leftBox = new BorderPane(toolBar, null,null,textSpace,null); //center, top, right, bottom, left
        leftBox.setId("toolbar");
        
        BorderPane contentBox = new BorderPane(tabBar, null, null,null , leftBox); //center, top, right, bottom, left
        contentBox.setId("root");
        root.getChildren().add(contentBox);
    
        this.scene = new Scene(root);

        scene.getStylesheets().add("dark.css"); //initial stylesheet - dark mode 
        
        stage.setTitle("PollutionViewer");
        stage.setScene(scene);
        stage.show();

        guiTabUpdates();
        handleUpdate();
        handleClick();
    }

    /**
     * Mouse coord label disappears when changing tab
     */
    private void guiTabUpdates()
    {
        tabBar.getSelectionModel().selectedItemProperty().addListener((observable, oldT, newT ) -> {
            if (newT == graphTab){
                mouseCoordLabel.setVisible(false);
            }
            else if(newT == mapTab){
                mouseCoordLabel.setVisible(true);
            }
            else{
                mouseCoordLabel.setVisible(false);
            }
        });
    }
    
    /**
     * Creates the check box for the filter
     * @param Pane container 
     */
    private void makeCheckBox(Pane parent){
        VBox holder = new VBox();
        holder.setPadding(new Insets(10));
        holder.setSpacing(10);
        parent.getChildren().add(holder);
        
        Label pLabel = new Label("Pollution Filters");
        pLabel.setId("titleLabel");
        holder.getChildren().add(pLabel);
        
        for (PollutionLevel level: PollutionLevel.values()){
        CheckBox box = new CheckBox(level.getLabel());
        box.setId("boxLabel");
        box.setSelected(true);
        holder.getChildren().add(box);
        
        box.setOnAction(event -> { 
            colFilters.replace(level.getHex(),box.isSelected());
            if(visualApplied){
                currentRegion.clearCanvas();
                applyVisualise(currentYear,currentType);}
            });
        } 
    }
    
    /**
     * Creates a graph and its interactive components
     * @param Pane parent container 
     */
    private void makePlotBox(Pane parent){
        
        VBox holder = new VBox();
        holder.setPadding(new Insets(10));
        holder.setSpacing(10);
        
        Label plotLabel = new Label("Graph Plotter");
        plotLabel.setId("titleLabel");
        
        Label zoneLabel = new Label("Select Zone:");
        ComboBox<String> zoneSelector = new ComboBox<>();
        zoneSelector.getItems().addAll("All Zones","North", "West", "East", "South",
        "Northeast", "Northwest", "Southeast", "Southwest", "Central");
        zoneSelector.setValue("All Zones");
        zoneSelector.setOnAction(event -> {
                                 currentZone = zoneSelector.getValue();
                                 });
        Label graphLabel = new Label("Select Graph:");
        ComboBox<String> graphSelector = new ComboBox<>();
        graphSelector.getItems().addAll("Average","Highest");
        graphSelector.setValue("Average");
        graphSelector.setOnAction(event -> {
                                 currentGraph = graphSelector.getValue();
                                 });        
        Button plotButton = new Button("Plot Graph");
        plotButton.setOnAction(e -> graphTab.plotGraph(currentRegion, zoneSelector.getValue(),graphSelector.getValue()));
        
        holder.getChildren().addAll(plotLabel,zoneLabel,zoneSelector,graphLabel,graphSelector,plotButton);
        
        parent.getChildren().add(holder);
    }
    
    /**
     * Creates the data pop up over the canvas
     * @param Marker marker that is clicked on
     * @param double x coordinate
     * @param double y coordinate 
     */
    private Popup createDataPopUp(Marker marker, double x, double y)
    {
        VBox holder = new VBox();
        holder.setId("dataBox");
        Popup markerPopUp = new Popup();
        Label markerData = new Label(marker.getInfo());
        markerData.setId("markerText");
        holder.getChildren().add(markerData);
        markerPopUp.getContent().add(holder);
        markerPopUp.setAnchorX(x + 5);
        markerPopUp.setAnchorY(y + 5);
        markerPopUp.show(stage);
        return markerPopUp;
    }
        
    /**
     * Creates the menu bar with interactive features
     * @param Pane parent container
     */
    private void makeMenuBar(Pane parent)
    {
        MenuBar menubar = new MenuBar();
        parent.getChildren().add(menubar);
        //Create visualise menu
        Menu yearMenu = new Menu("Visualise");
        
        //Create nested Menus for data types
        Menu no2View = new Menu("NO2");
        Menu pm25View = new Menu("pm2.5");
        Menu pm10View = new Menu("pm10");
        yearMenu.getItems().addAll(no2View,pm25View,pm10View);
        //pm10 years
        for(int i = 2018; i< 2024; i++){
            final String year = Integer.toString(i);
            MenuItem item = new MenuItem(year);
            item.setOnAction(e -> handleVisualiseClick(item,year)); 
            //Add year menu item to data type
            pm10View.getItems().add(item);
        }
        
        // Add live option with warning for PM10
        MenuItem pm10Live = new MenuItem("live");
        pm10Live.setOnAction(e -> handleLiveWarning("pm10"));
        pm10View.getItems().add(pm10Live);
        
        //no2 years
        for(int i = 2018; i< 2024; i++){
            final String year = Integer.toString(i);
            MenuItem item = new MenuItem(year);
            item.setOnAction(e -> handleVisualiseClick(item,year)); 
            //Add year menu item to data type
            no2View.getItems().add(item);
        }
        
        // Add live option with warning for NO2
        MenuItem no2Live = new MenuItem("live");
        no2Live.setOnAction(e -> handleLiveWarning("no2"));
        no2View.getItems().add(no2Live);
        
        //pm2.5 years
        for(int i = 2018; i< 2024; i++){
            final String year = Integer.toString(i);
            MenuItem item = new MenuItem(year);
            item.setOnAction(e -> handleVisualiseClick(item,year)); 
            //Add year menu item to data type
            pm25View.getItems().add(item);
        }
        
        // Add live option with warning for PM2.5
        MenuItem pm25Live = new MenuItem("live");
        pm25Live.setOnAction(e -> handleLiveWarning("pm25"));
        pm25View.getItems().add(pm25Live);
        
        MenuItem clearItem = new MenuItem("Clear");
        clearItem.setOnAction(this::clearMap);
        yearMenu.getItems().add(clearItem);
        
        Menu locationMenu = new Menu("Location");
        
        for(int i = 0; i < regionManager.getRegionAmount();i++){
            final String location = regionManager.getRegionIndex(i).getRegionName();
            MenuItem item = new MenuItem(location);
            item.setOnAction(e -> handleLocationChange(location));
            locationMenu.getItems().add(item);
        }
        
        //Create help menu
        Menu helpMenu = new Menu("Help");
        
        MenuItem instructionsItem =  new MenuItem("Instructions");
        instructionsItem.setOnAction(e -> showInstructions());
        helpMenu.getItems().add(instructionsItem);
        menubar.setPrefHeight(10);
        
        //Create help menu
        Menu visualMenu = new Menu("Visuals"); 
        MenuItem lightItem =  new MenuItem("Light Mode");
        lightItem.setOnAction(e -> {scene.getStylesheets().remove(0);
                                    scene.getStylesheets().add("light.css");});
        MenuItem darkItem =  new MenuItem("Dark Mode");
        darkItem.setOnAction(e -> {scene.getStylesheets().remove(0);
                                    scene.getStylesheets().add("dark.css");});
        visualMenu.getItems().addAll(darkItem,lightItem);

        
        Menu searchMenu = new Menu("Search");
        MenuItem searchCoord = new MenuItem("Coordinates");
        MenuItem searchGrid = new MenuItem("Gridcode");
        searchMenu.getItems().addAll(searchCoord,searchGrid);
        searchCoord.setOnAction(e -> handlePopup(searchCoord));
        searchGrid.setOnAction(e_-> handlePopup(searchGrid));
        
        menubar.setPrefHeight(10);
        menubar.getMenus().addAll(yearMenu,locationMenu,visualMenu,searchMenu, helpMenu);
        
    }
    
    /**
     * Warns the user before fetching live data.
     */
    private void handleLiveWarning(String pollutant) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Live Data Warning");
        alert.setHeaderText("Fetching live data may take a long time.");
        alert.setContentText("Do you want to continue?");
        
        alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            List<double[]> locations = UKLocationGenerator.generateUKLocations(currentRegion.getRegionName());

                            // Pass a callback function to handle when data loading is complete
                            LiveDataCSVWriter.fetchAndWriteCSV(locations, pollutant, 
                                (year, type) -> {
                                        // Load the data into the data repository
                                        dataRepo.loadLiveData(type);

                                        // Apply visualization with the loaded data
                                        applyVisualise(year, type);
                                });

                        } catch (Exception e) {
                            showErrorPopup("Error fetching live data: " + e.getMessage());
                        }
                    }
            });
    }
    
    /**
     * Displays an error popup with the given message.
     */
    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }  
    
    /**
     * Finds the pollution value at the x and y of the mouse
     * @param double adjustedX x coordinate
     * @param double adjustedY y coordinate
     * @return String marker value or Missing 
     */
    private String findPollutionValue(double adjustedX, double adjustedY){
        if (visualApplied){
            for(Marker marker : markers){
                if(marker.inRange(adjustedX,adjustedY)){
                    return Double.toString(marker.getValue());
                }
            }
        }
        else{
            return "0";
        }
        return "Missing";
    }
}  

