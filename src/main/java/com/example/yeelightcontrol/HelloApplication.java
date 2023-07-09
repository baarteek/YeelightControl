package com.example.yeelightcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 700);
        scene.getStylesheets().add(getClass().getResource("/com/example/yeelightcontrol/css/style.css").toExternalForm());
        stage.setResizable(false);
        Image applicationIcon = new Image(getClass().getResourceAsStream("/com/example/yeelightcontrol/icons/bulb.png"));
        stage.getIcons().add(applicationIcon);
        stage.setTitle("Yeelight Control");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}