package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class PropertiesController {
    @FXML
    private ChoiceBox MazechoiceBox;
    @FXML
    private ChoiceBox SolvechoiceBox;
    private Stage OptionsStage;
    MyViewController myViewController;

    public void setMyViewController(MyViewController myViewController) {
        this.myViewController = myViewController;
    }

    public void setOptionStage(Stage s) {
        OptionsStage = s;
    }


    public void BackToMainView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent pane = fxmlLoader.load();
        String Generator = MazechoiceBox.getSelectionModel().getSelectedItem().toString();
        String Solver = SolvechoiceBox.getSelectionModel().getSelectedItem().toString();
        myViewController.getProperties().setProperty("mazeGeneratingAlgorithm", Generator);
        System.out.println(myViewController.getProperties().getProperty("mazeGeneratingAlgorithm"));
        myViewController.getProperties().setProperty("mazeSearchingAlgorithm", Solver);
        myViewController.getProperties();
        System.out.println(myViewController.getProperties().getProperty("mazeSearchingAlgorithm"));
        myViewController.getProperties().store(new FileWriter("resources/config.properties"), "store to properties file");
        myViewController.newMaze();

        showAlert("Properties Changed");
        OptionsStage.close();


    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    public void setMazeChoiceBox() {
        MazechoiceBox.valueProperty().setValue(myViewController.getProperties().getProperty("mazeGeneratingAlgorithm", "MyMazeGenerator"));
    }

    public void setSolveChoiceBox() {
        SolvechoiceBox.valueProperty().setValue(myViewController.getProperties().getProperty("MazeSolver", "BestFirstSearch"));
    }
}
