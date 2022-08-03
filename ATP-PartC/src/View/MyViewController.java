package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class MyViewController implements Initializable, IView, Observer {
    private MyViewModel myViewModel;
    private boolean propertiesChanged = false;
    Properties properties;


    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Label playerRow;
    @FXML
    public Label playerCol;
    @FXML
    public Pane BoardPane;
    @FXML


    private final FileChooser fileChooser = new FileChooser();
    private static MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("resources/music/Single-footstep.mp3").toURI().toString()));
    ; //Media player


    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();


    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);

        try {
            properties = new Properties();
            properties.load(new FileInputStream("./resources/config.properties"));
        } catch (Exception e) {
            System.out.println("Properties file not found");
        }
    }

    public void generateMaze(ActionEvent actionEvent) {

        if (propertiesChanged) {
            myViewModel.refreshProperties();
            propertiesChanged = false;
        }
        if (!textField_mazeRows.getText().matches("\\d*")) {
            textField_mazeRows.setText("10");
            popAlert("Error", "Numbers Only!");
        }
        if (!textField_mazeColumns.getText().matches("\\d*")) {
            textField_mazeColumns.setText("10");
            popAlert("Error", "Numbers Only!");
        }
        int rows = Integer.parseInt(textField_mazeRows.getText());
        int cols = Integer.parseInt(textField_mazeColumns.getText());

        myViewModel.generateMaze(rows, cols);

        mazeDisplayer.setRoundFirst(true);
        mazeDisplayer.requestFocus();
        mazeDisplayer.widthProperty().bind(BoardPane.widthProperty());
        mazeDisplayer.heightProperty().bind(BoardPane.heightProperty());


        mazeDisplayer.drawMaze(myViewModel.getmaze());

        Main.startMusic.setAutoPlay(true);
        mazeDisplayer.requestFocus();


    }


    public Properties getProperties() {
        return properties;
    }


    public void solveMaze(ActionEvent actionEvent) {
        if (myViewModel.getmaze() == null) {
            popAlert("Error", "No maze to solve!");
        } else {
//            popAlert("Solve", "Solving maze...");

            myViewModel.solveMaze(mazeDisplayer.getPlayerRow(), mazeDisplayer.getPlayerCol());
            mazeDisplayer.setSolution(myViewModel.getSolution());
        }
        mazeDisplayer.requestFocus();
    }


    public void keyPressed(KeyEvent keyEvent) {
        if (myViewModel.getmaze() != null) {
            myViewModel.movePlayer(keyEvent);
            keyEvent.consume();
            KeyCode[] key = {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.NUMPAD2, KeyCode.NUMPAD8, KeyCode.NUMPAD4, KeyCode.NUMPAD6, KeyCode.NUMPAD7, KeyCode.NUMPAD9, KeyCode.NUMPAD1, KeyCode.NUMPAD3};
            ArrayList<KeyCode> keys = new ArrayList<>(Arrays.asList(key));
            if( keys.contains(keyEvent.getCode())){
                mediaPlayer = new MediaPlayer(new Media(new File("resources/music/Single-footstep.mp3").toURI().toString()));
                mediaPlayer.play();
            }

        }

    }

    public void setPlayerPosition(int row, int col) {
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    @Override
    public void update(Observable o, Object arg) {
        if ("mazeFromFile".equals(arg)) {
            Maze maze = myViewModel.getmaze();
            setUpdatePlayerRow(maze.getStartPosition().getRowIndex());
            setUpdatePlayerCol(maze.getStartPosition().getColumnIndex());
            mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
            mazeDisplayer.setGoalPosition(myViewModel.getEndRow(), myViewModel.getEndCol());
            mazeDisplayer.drawMaze(maze);
            mazeDisplayer.drawFinished(myViewModel.isSolved());
        }


        if ("generateMaze".equals(arg)) {
            int StartRow = myViewModel.getStartRow();
            int StartCol = myViewModel.getStartCol();
            try {
                setUpdatePlayerRow(StartRow);
                setUpdatePlayerCol(StartCol);
                mazeDisplayer.drawMaze(myViewModel.getmaze());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //if we reached goal
            if (this.mazeDisplayer.getMaze() == myViewModel.getMaze()) {
                if (mazeDisplayer.getPlayerRow() == myViewModel.getPlayerRow() && mazeDisplayer.getPlayerCol() == myViewModel.getPlayerCol()) {
                    myViewModel.getSolution();
                } else {
                    setUpdatePlayerRow(myViewModel.getPlayerRow());
                    setUpdatePlayerCol(myViewModel.getPlayerCol());
                    this.mazeDisplayer.drawFinished(myViewModel.isSolved());
                    this.mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());

                    if (myViewModel.isSolved()) {
                        this.mazeDisplayer.setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
                        mediaPlayer = new MediaPlayer(new Media(new File("./resources/music/applause-2.mp3").toURI().toString()));
                        mediaPlayer.play();
                    }

                }
            } else {
                this.mazeDisplayer.setMaze(myViewModel.getMaze());
                try {
                    setUpdatePlayerCol(myViewModel.getPlayerCol());
                    setUpdatePlayerRow(myViewModel.getPlayerRow());
                    mazeDisplayer.drawMaze(myViewModel.getmaze());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }


    public void popAlert(String title, String message) {

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(300);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close this window");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.getIcons().add(new Image("resources/Background/maze.png"));
        window.showAndWait();
    }


    public void play(ActionEvent actionEvent) {
        Main.startMusic.setMute(false);
        mediaPlayer.setMute(false);
        mazeDisplayer.requestFocus();


    }

    public void mute(ActionEvent actionEvent) {
        Main.startMusic.setMute(true);
        mediaPlayer.setMute(true);
        mazeDisplayer.requestFocus();
    }

    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure that you want to exit?");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("resources/Background/maze.png"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            myViewModel.Exit();
            System.exit(0);
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (myViewModel.getmaze() != null) {
            int rows = myViewModel.getmaze().getMaze().length;
            int cols = myViewModel.getmaze().getMaze()[0].length;
            if (myViewModel.getmaze() != null) {
                int Size = Math.max(rows, cols);
                double mouseX = mouseDragHandler(Size, mazeDisplayer.getHeight(), rows, mouseEvent.getX(), mazeDisplayer.getWidth() / Size);
                double mouseY = mouseDragHandler(Size, mazeDisplayer.getWidth(), cols, mouseEvent.getY(), mazeDisplayer.getHeight() / Size);
                myViewModel.movePlayer(mouseX, mouseY);
            }
        }

    }

    private double mouseDragHandler(int PaneSize, double canvasSize, int mazeSize, double mouseEvent, double temp) {
        double cellSize = canvasSize / PaneSize;
        double start = (canvasSize / 2 - (cellSize * mazeSize / 2)) / cellSize;
        return (int) (((mouseEvent) - start) / temp);
    }

    public void mouseScroll(ScrollEvent scrollEvent) {
        if (myViewModel.getmaze() != null) {
            if (scrollEvent.isControlDown()) {
                double zoomDelta = 1.25;
                if (scrollEvent.getDeltaY() <= 0) {
                    zoomDelta = 1 / zoomDelta;
                }

                Scale scale = new Scale();
                scale.setX(BoardPane.getScaleX() * zoomDelta);
                scale.setY(BoardPane.getScaleY() * zoomDelta);
                scale.setPivotX(BoardPane.getScaleX());
                scale.setPivotY(BoardPane.getScaleY());
                BoardPane.getTransforms().add(scale);
            }
        }
    }

    public void enterPropWindow(ActionEvent actionEvent) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/FxmlProperties.fxml"));
            Scene scene = BoardPane.getScene();
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Properties");
            stage.setScene(new Scene(root, 390, 220));
            Image image = new Image("resources/Background/settings.png");
            stage.getIcons().add(image);


            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            root.setStyle(
                    "-fx-background-image: url(" +
                            "'/resources/Background/cosmic.jpg'" +
                            "); " +
                            "-fx-background-size: cover;"
            );


            stage.show();
            PropertiesController optionsController = fxmlLoader.getController();
            optionsController.setMyViewController(this);
            optionsController.setOptionStage(stage);
            optionsController.setMazeChoiceBox();
            optionsController.setSolveChoiceBox();
            propertiesChanged = true;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void saveMaze(ActionEvent actionEvent) {
        if (myViewModel.getmaze() != null) {
            Window stage = BoardPane.getScene().getWindow();
            fileChooser.setTitle("Save Maze");
            fileChooser.setInitialFileName("maze");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("textfile", "*.txt"));
            try {
                File file = fileChooser.showSaveDialog(stage);
                myViewModel.saveMaze(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            popAlert("Error", "You need to generate a maze first");
        }


    }

    public void HelpClickHandler(ActionEvent actionEvent) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/Help.fxml"));
            Scene myScene = BoardPane.getScene();
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Help");
            Image image = new Image("/resources/Background/maze.png");
            stage.getIcons().add(image);
            stage.setScene(new Scene(root));

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            root.setStyle(
                    "-fx-background-image: url(" +
                            "'resources/Background/helpScreen.png'" +
                            "); " +
                            "-fx-background-size: cover;"
            );


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void aboutClickHandler(ActionEvent actionEvent) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("View/Help.fxml"));
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("About");
            Image image = new Image("/resources/Background/maze.png");
            stage.getIcons().add(image);
            stage.setScene(new Scene(root));


            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            /**
             * tochange the background of the about window
             */
            root.setStyle(
                    "-fx-background-image: url(" +
                            "'resources/Background/about.png'" +
                            "); " +
                            "-fx-background-size: stretch"
            );


            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void NewMaze(ActionEvent actionEvent) {
        Main.startMusic.setMute(true);
        mazeDisplayer.getGraphicsContext2D().clearRect(0, 0, mazeDisplayer.getWidth(), mazeDisplayer.getHeight());
    }

    public void newMaze() {
        Main.startMusic.setMute(true);
        mazeDisplayer.getGraphicsContext2D().clearRect(0, 0, mazeDisplayer.getWidth(), mazeDisplayer.getHeight());

        if (propertiesChanged) {
            myViewModel.refreshProperties();
            propertiesChanged = false;
        }

        playerRow.requestFocus();
        textField_mazeRows.requestFocus();
    }


    public void loadMazeHandler(ActionEvent actionEvent) {
        Window stage = BoardPane.getScene().getWindow();
        fileChooser.setTitle("Load File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documents", "*.docx", "*.txt", "*.pdf"));
        File file = fileChooser.showOpenDialog(stage);
        myViewModel.loadMaze(file);

    }

}
