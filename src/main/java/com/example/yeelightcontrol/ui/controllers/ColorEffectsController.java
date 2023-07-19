package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ColorEffectsController implements Initializable {
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToSettingsViewFxml = "/com/example/yeelightcontrol/fxml/settings-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private YeelightActions bulbActions;
    @FXML
    private Label nameTab;
    @FXML
    private Slider delaySlider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTab.setText("Color Effects");
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

    public void goToSettingsView() {
        disconnectWithDevice();
        switchToSettingsView();
    }

    private void switchToSettingsView() {
        Stage stage = (Stage) nameTab.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.switchToScene(pathToSettingsViewFxml, pathToCssFile);
    }

    public void turnOnPulseEffect() {
        int pulseSpeed = (int) delaySlider.getValue();
        tryTurnOnPulseEffect(pulseSpeed);
    }

    private void tryTurnOnPulseEffect(int pulseSpeed) {
        try {
            bulbActions.pulseEffect(pulseSpeed);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Pulse Effect Error", "An error occurred while trying to enable the pulsing effect on the device.");
        }
    }

    public void turnOnNightMode() {
        try {
            bulbActions.nightMode();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Night Mode Error", "An error occurred while trying to enable Night Mode on device.");
        }
    }

    public void turnOnReadMode() {
        try {
            bulbActions.readMode();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Read Mode Error", "An error occurred while trying to enable Read Mode on device.");
        }
    }

    public void turnOnRomanceMode() {
        try {
            bulbActions.romanceMode();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Romance Mode Error", "An error occurred while trying to enable Romance Mode on device.");
        }
    }

    public void turnOnFullIntensity() {
        try {
            bulbActions.fullIntensity();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Full Intensity Error", "An error occurred while trying to enable Full Intensity on device.");
        }
    }
}
