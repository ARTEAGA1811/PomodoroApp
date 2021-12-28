package ec.edu.epn.pomodoroapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loaderPrincipal = new FXMLLoader(getClass().getResource("Guipomodoro.fxml"));
        Parent rootPrincipal = loaderPrincipal.load();

        //Get controller of Principal_control
        Principal_control principalController = loaderPrincipal.getController();

        //Set the stage
        Stage stgPrincipal = new Stage();
        stgPrincipal.setScene(new Scene(rootPrincipal));
        stgPrincipal.setTitle("My Pomodoro!");
        stgPrincipal.setResizable(false);
        stgPrincipal.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}