/**
 * This is the Main class of the program.
 * It launches the GUI and initiates the Controller class.
 *
 * @author Alexandra Härnström
 * @version 1
 */

package com.example.xmlparser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Send an XML");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            Controller controller = fxmlLoader.getController();
            controller.exitProgram();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}