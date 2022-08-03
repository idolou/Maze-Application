package View;

//<?import View.MazeDisplayer?>
//<MazeDisplayer fx:id="mazeDisplayer" height="400.0" width="400.0" />

import ViewModel.MyViewModel;
import Model.MyModel;
import Model.IModel;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.stage.WindowEvent;
import java.io.File;
import java.util.Optional;


public class Main extends Application {

    public static MediaPlayer startMusic;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Maze App");
        Image image = new Image("/resources/Background/maze.png");
        primaryStage.getIcons().add(image);

        Media media = new Media(new File("resources/music/Star_Wars_Main_Theme_Song.mp3").toURI().toString());
        startMusic = new MediaPlayer(media);
        startMusic.setVolume(0.1);
//        startMusic.setAutoPlay(true);
        startMusic.setCycleCount(MediaPlayer.INDEFINITE);



        /** set the app size to the screen size**/
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        root.setStyle(
                "-fx-background-image: url(" +
                        "'/resources/Background/cosmic.jpg'" +
                        "); " +
                        "-fx-background-size: cover;"
        );



        Scene scene = new Scene(root, 650, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController myViewController = fxmlLoader.getController();
        myViewController.setMyViewModel(viewModel);
        viewModel.addObserver(myViewController);







        //close the program if the user click on the close button
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure that you want to exit?");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("resources/Background/maze.png"));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    model.exit();
                } else {
                    windowEvent.consume();
                }
            }
        });


    }

    public void drawFinish(){

    }


    public static void main(String[] args) {
        launch(args);
    }

}
