package com.example.yeelightcontrol.ui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public SceneSwitcher(Stage stage) {
        this.stage = stage;
    }

    public void switchToScene(String fxmlPath, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            root = loader.load();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("View change error", "An error occurred while changing the view");
            e.printStackTrace();
        }
    }
}
