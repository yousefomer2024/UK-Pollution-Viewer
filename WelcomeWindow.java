import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Label.*;

/**
 * The `WelcomeWindow` class creates a modal window that displays a series of instructions to the user upon starting the London Air Pollution Visualizer application.
 * The instructions guide the user through the features of the tool and explain how to interact with the map and data.
 * The window includes navigation buttons to move through the pages of instructions and a progress bar to indicate the user's position.
 * 
 * The window is displayed on top of the main application window, blocking interaction with the main window until the user finishes the tutorial.
 * 
 * @author Ozgur Dorunay
 * @version 24/03/25
 */
public class WelcomeWindow extends Stage {

    private int pageIndex = 0; // Keeps track of the current instruction page index
    private Label instructionLabel; // Label to display instructions
    private Button nextButton; // Button to navigate to the next instruction
    private Button finishButton; // Button to finish the tutorial and close the window
    private Label progressLabel; // Label displaying the current page out of total pages
    private ProgressBar progressBar; // Progress bar showing the user's progress

    // Array of instruction texts to be displayed in the tutorial
    private final String[] instructions = {
        "Welcome to the London Air Pollution Visualizer!\n\nThis tool allows you to explore and visualize air pollution levels across London. It provides interactive features to help you understand pollution trends and their impact on different areas.",
        "You can view pollution levels across different regions in London on the map. Use the year selection to explore trends over time.",
        "Use the 'Visualisation' menu to overlay pollution data on the map. Use the 'Clear' button to reset the map and remove any visualized pollution data.",
        "Click on a pollution marker to view more detailed information about that location.",
        "You can apply filters to visualize pollution levels (e.g., NO₂, PM10, PM2.5) using color-coded markers. Purple represents high pollution, while green indicates low pollution.",
        "You can select different regions of London, such as North, South, East, and West, for a more detailed focus on specific areas.",
        "The 'Statistics' tab displays data points related to the pollution levels in each region, while the 'Graph Plotter' tab allows you to visualize pollution trends over time.",
        "Press 'Finish' to start exploring the map."
    };

    /**
     * Constructor for the `WelcomeWindow` class. Initializes the window with UI components and sets up the layout.
     * @param stage The main window stage that this window will be associated with
     */
    public WelcomeWindow(Stage stage) {
        // Window title and modality settings
        setTitle("Welcome to the London Pollution Viewer!");
        initModality(Modality.APPLICATION_MODAL); // Blocks interaction with the main window
        initOwner(stage);

        // Initialize instruction label with the first instruction
        instructionLabel = new Label(instructions[pageIndex]);
        instructionLabel.setWrapText(true);
        instructionLabel.setAlignment(javafx.geometry.Pos.CENTER);  // Center text inside label
        instructionLabel.setStyle("-fx-text-alignment: center;");  // Ensure text is centered
        instructionLabel.setMaxWidth(360);

        // Initialize the navigation buttons
        nextButton = new Button("Next");
        finishButton = new Button("Finish");

        // Initialize the progress label and progress bar
        progressLabel = new Label(getProgressText());
        progressBar = new ProgressBar((double) pageIndex / (instructions.length - 1));
        progressBar.setPrefWidth(70);
        progressBar.setStyle("-fx-accent: blue");

        // VBox for progress label and progress bar
        VBox progressBox = new VBox(10, progressLabel, progressBar);
        progressBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        // Button actions
        nextButton.setOnAction(e -> showNextPage());
        finishButton.setOnAction(e -> close()); // Close the window on finish

        // Layout for the buttons
        HBox buttonBox = new HBox(10, nextButton, finishButton);
        buttonBox.setPadding(new javafx.geometry.Insets(10));
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Layout for the bottom section (buttons and progress indicator)
        BorderPane bottomPane = new BorderPane();
        bottomPane.setCenter(buttonBox); // Place buttons in the center
        bottomPane.setRight(progressBox); // Place progress indicator to the right
        bottomPane.setPadding(new javafx.geometry.Insets(10));

        // Main layout for the window (instruction label in the center, buttons at the bottom)
        BorderPane layout = new BorderPane();
        layout.setCenter(instructionLabel);  // Instructions in the middle
        layout.setBottom(bottomPane);  // Buttons and progress indicator at the bottom
        BorderPane.setAlignment(instructionLabel, javafx.geometry.Pos.CENTER);

        // Set the scene and display the window
        Scene scene = new Scene(layout, 400, 200);
        setScene(scene);
    }

    /**
     * Handles the action of navigating to the next instruction page.
     * Updates the instruction text, progress text, and progress bar.
     */
    private void showNextPage() {
        if (pageIndex < instructions.length - 1) {
            pageIndex++; // Increment the page index
            instructionLabel.setText(instructions[pageIndex]);
            progressLabel.setText(getProgressText());
            progressBar.setProgress((double) pageIndex / (instructions.length - 1));
        }
        // Disable the "Next" button when on the last page
        if (pageIndex == instructions.length - 1) {
            nextButton.setDisable(true);
        }
    }

    /**
     * Returns the progress text for the progress indicator.
     * @return A string representing the current page out of the total number of pages.
     */
    private String getProgressText() {
        return "Page " + (pageIndex + 1) + " of " + instructions.length;
    }
}
