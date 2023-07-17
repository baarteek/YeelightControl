package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WeatherController implements Initializable {
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private YeelightActions bulbActions;
    @FXML
    private Label nameTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTab.setText("Weather");
        connectToDevice();
    }

    private void connectToDevice() {
        bulb = new YeelightBulb(DeviceInfo.name, DeviceInfo.ip);
        try {
            bulb.connect();
            bulbActions = new YeelightActions(bulb);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Connect Error", "An error occurred while trying to connect to the device. Please try again.");
        }
    }


    private void disconnectWithDevice() {
        try {
            bulb.disconnect();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Disconnect Error", "An error occurred while trying to disconnect with the device.");
        }
    }

    public void backToMainView() {
        disconnectWithDevice();
        switchToMainView();
    }

    private void switchToMainView() {
        Stage stage = (Stage) nameTab.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.switchToScene(pathToMainViewFxml, pathToCssFile);
    }
}
