package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.ui.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    private final String pathToHelloViewFxml = "/com/example/yeelightcontrol/fxml/hello-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private YeelightActions bulbActions;
    @FXML
    private Label nameDeviceLabel;
    @FXML
    private Label addressIPLabel;
    @FXML
    private ToggleButton powerToggleButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameDeviceLabel.setText(DeviceInfo.name);
        addressIPLabel.setText(DeviceInfo.ip);

        connectToDevice();
        initToggleButton();
    }

    private void connectToDevice() {
        bulb = new YeelightBulb(DeviceInfo.name, DeviceInfo.ip);
        try {
            bulb.connect();
            bulbActions = new YeelightActions(bulb);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error", "An error occurred while trying to connect to the device. Please try again.");
        }
    }

    private void turnOnDevice() {
        try {
            bulbActions.turnOn();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error",  "Error while trying to turn on the device.");
        }
    }

    private void turnOffDevice() {
        try {
            bulbActions.turnOff();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error",  "Error while trying to turn off the device.");
        }
    }

    private void initToggleButton() {
        powerToggleButton.setText("OFF");
        powerToggleButton.setSelected(true);
        turnOnDevice();
    }

    public void backToHelloWorld() {
        Stage stage = (Stage) nameDeviceLabel.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.switchToScene(pathToHelloViewFxml, pathToCssFile);
    }

    public void powerToggleDeviceButtonClicked() {
        if(powerToggleButton.isSelected()) {
            turnOnDevice();
            powerToggleButton.setText("OFF");
        } else {
            turnOffDevice();
            powerToggleButton.setText("ON");
        }
    }

    public void powerToggleDeviceClicked() {
        powerToggleButton.fire();
    }
}
