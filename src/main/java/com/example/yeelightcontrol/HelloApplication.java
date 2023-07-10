package com.example.yeelightcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private final String pathToHelloView = "/com/example/yeelightcontrol/fxml/hello-view.fxml";
    private final String pathToIcon = "/com/example/yeelightcontrol/icons/bulb.png";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(pathToHelloView));
        Scene scene = new Scene(fxmlLoader.load(), 500, 700);
        scene.getStylesheets().add(getClass().getResource(pathToCssFile).toExternalForm());
        stage.setResizable(false);
        Image applicationIcon = new Image(getClass().getResourceAsStream(pathToIcon));
        stage.getIcons().add(applicationIcon);
        stage.setTitle("Yeelight Control");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}