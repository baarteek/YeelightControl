package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeviceAdjustmentController implements Initializable {
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToSettingsViewFxml = "/com/example/yeelightcontrol/fxml/settings-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private YeelightActions bulbActions;
    @FXML
    private Label nameTab;
    @FXML
    private ChoiceBox transitionEffectChoiceBox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Slider brightnessSlider;
    @FXML
    private Slider colorTemperatureSlider;
    @FXML
    private Slider durationSlider;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTab.setText("Device Adjustments");
        setTransitionEffectChoiceBox();
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

    private void setTransitionEffectChoiceBox() {
        transitionEffectChoiceBox.setStyle("-fx-text-fill: black;");
        transitionEffectChoiceBox.getItems().addAll("Smooth", "Sudden");
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

    public void setColor() {
        Color color = colorPicker.getValue();
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        trySetColor(red, green, blue);
    }

    private void trySetColor(int red, int green, int blue) {
        try {
            bulbActions.setColorRGB(red, green, blue);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Set Color Error", "An error occurred while trying to set the color on the device.");
        }
    }

    public void setBrightness() {
        int brightness = (int) brightnessSlider.getValue();
        trySetBrightness(brightness);
    }

    private void trySetBrightness(int value) {
        try {
            bulbActions.setBrightness(value);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Set Brightness Error", "An error occurred while trying to set the brightness on your device.");
        }
    }

    public void setColorTemperature() {
        int colorTemperature = (int) colorTemperatureSlider.getValue();
        trySetColorTemperature(colorTemperature);
    }

    private void trySetColorTemperature(int value) {
        try {
            bulbActions.setColorTemperature(value);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Set Color Temperature Error", "An error occurred while trying to set the color temperature on the device.");
        }
    }

    public void setDurationOfTransition() {
        int duration = (int) durationSlider.getValue();
        bulbActions.setDuration(duration);
    }

    public void setTransitionEffect() {
        String transitionEffect = (String) transitionEffectChoiceBox.getValue();
        bulbActions.changeTransitionEffect(transitionEffect);
    }
}
