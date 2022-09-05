package puzzles.hoppers.gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

/**
 * This class is used to run simulations of the hoppers puzzle in a graphical user interface
 * @author Jake Edelstein
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {

    // various private states
    private static HoppersModel model;
    private boolean initialized = false;
    private String currentFile;
    private Button[][] buttons;
    private Label topText;
    private Stage stage;
    private BorderPane bp;
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    // the images used in the GUI
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    /**
     * Initialize the simulation
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        model = new HoppersModel();
        model.addObserver(this);
        model.load(filename);
        this.currentFile = filename;
    }

    /**
     * Build the stage and its components
     * @param stage the stage
     * @throws Exception any exception that may occur, mainly IOExceptions
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        bp = new BorderPane();
        this.topText = makeTopText();
        BorderPane.setAlignment(this.topText, Pos.CENTER);
        bp.setTop(this.topText);
        GridPane grid = makeGrid();
        BorderPane.setAlignment(grid, Pos.CENTER);
        bp.setCenter(grid);
        bp.setBottom(makeUtilButtons());
        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        this.initialized = true;
    }

    /**
     * Create a button that is used for the game
     * @param coordinates the coordinates of the button
     * @param status whether a space has a green frog, red frog, no frog, or water
     * @return a button
     */
    public Button makeFrogButton(Coordinates coordinates, char status){
        Button b = new Button();
        b.setMinSize(75, 75);
        b.setMaxSize(75, 75);
        if (status == HoppersConfig.NO_FROG){
            b.setGraphic(new ImageView(lilyPad));
        } else if (status == HoppersConfig.GREEN_FROG){
            b.setGraphic(new ImageView(greenFrog));
        } else if (status == HoppersConfig.RED_FROG){
            b.setGraphic(new ImageView(redFrog));
        } else {
            b.setGraphic(new ImageView(water));
        }
        // run the select() method when a button is clicked
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.select(coordinates);
            }
        });
        return b;
    }

    /**
     * Makes a GridPane to store the buttons as well as a 2D array to keep track of each button's location
     * @return a GridPane
     */
    public GridPane makeGrid(){
        GridPane grid = new GridPane();
        buttons = new Button[model.getCurrentConfig().getNumRows()][model.getCurrentConfig().getNumCols()];
        for (int r = 0; r < model.getCurrentConfig().getNumRows(); r++){
            for (int c = 0; c < model.getCurrentConfig().getNumCols(); c++){
                buttons[r][c] = makeFrogButton(new Coordinates(r, c), model.getCurrentConfig().getGrid()[r][c]);
                grid.add(buttons[r][c], c, r);
            }
        }
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    /**
     * Create the Label displayed at the top of the window
     * @return a label
     */
    public Label makeTopText(){
        Label label = new Label("Loaded: " + currentFile);
        label.setStyle("""
                -fx-font: 18px Menlo;
                -fx-border-radius: 6;
                """);
        return label;
    }

    /**
     * Create the load, reset, and hint buttons, and their corresponding methods
     * @return an HBox containing the buttons
     */
    public HBox makeUtilButtons(){
        Button load = new Button("Load");
        load.setStyle("""
                -fx-font: 16px Menlo;
                -fx-border-radius: 2;
                """);
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose a Hoppers File");
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null){
                    currentFile = String.valueOf(selectedFile);
                    model.load(String.valueOf(selectedFile));
                    bp.setCenter(makeGrid());
                    buttons = new Button[model.getCurrentConfig().getNumRows()][model.getCurrentConfig().getNumCols()];
                    stage.sizeToScene();
                }
            }
        });

        Button reset = new Button("Reset");
        reset.setStyle("""
                -fx-font: 16px Menlo;
                -fx-border-radius: 2;
                """);
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.load(currentFile);
            }
        });

        Button hint = new Button("Hint");
        hint.setStyle("""
                -fx-font: 16px Menlo;
                -fx-border-radius: 2;
                """);
        hint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.hint();
            }
        });

        HBox hBox = new HBox(load, reset, hint);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    /**
     * Refresh the GUI with the latest changes to the model
     * @param hoppersModel the model
     * @param msg the message to display at the top of the window
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        if (!this.initialized){
            return;
        }
        this.topText.setText(msg);
        // create a new, blank board to account for loading a new file
        buttons = new Button[model.getCurrentConfig().getNumRows()][model.getCurrentConfig().getNumCols()];
        // refresh the board
        for (int r = 0; r < model.getCurrentConfig().getNumRows(); r++){
            for (int c = 0; c < model.getCurrentConfig().getNumCols(); c++){
                char currentStatus = model.getCurrentConfig().getGrid()[r][c];
                buttons[r][c] = makeFrogButton(new Coordinates(r, c), currentStatus);
                bp.setCenter(makeGrid());
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
