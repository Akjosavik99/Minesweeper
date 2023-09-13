package minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MinesweeperApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("App.fxml"))));
        primaryStage.getIcons().add(new Image(MinesweeperApp.class.getResourceAsStream("minesweeperIcon.png")));
        primaryStage.show();
    }
    
}
