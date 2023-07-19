package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.retrievers.GoldPriceRetriever;
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
    private final String pathToExchangeRatesViewFxml = "/com/example/yeelightcontrol/fxml/exchange-rates-view.fxml";
    private final String pathToMorseCodeViewFxml = "/com/example/yeelightcontrol/fxml/morse-code-view.fxml";
    private final String pathToDeviceAdjustmentViewFxml = "/com/example/yeelightcontrol/fxml/device-adjustment-view.fxml";
    private final String pathToWeatherViewFxml = "/com/example/yeelightcontrol/fxml/weather-view.fxml";
    private final String pathToColorEffectsViewFxml = "/com/example/yeelightcontrol/fxml/color-effects-view.fxml";
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

    public void setColorBasedOnGoldPrice() {
        trySetColorBasedOnGoldPrice();
        showGoldPrice();
    }

    private void trySetColorBasedOnGoldPrice() {
        try {
            bulbActions.adjustBrightnessAndColorBasedOnGoldPrice();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Gold Price Error", "Error while setting device parameters.");
        }
    }

    private void showGoldPrice() {
        double goldPrice = getGoldPrice();
        String message = "Today's gold price is: " + goldPrice + " PLN per gram.\n\nThe color of the device was established based on the price of gold.";
        DialogHelper.showInformationDialog("Gold Price", message);
    }

    private double getGoldPrice() {
        GoldPriceRetriever goldPrice = new GoldPriceRetriever();
        try {
            return goldPrice.getGoldPrice();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Gold Price Error", "Error getting gold price.");
        }
        return 0;
    }

    public void switchToExchangeRateView() {
        switchToAnotherScene(pathToExchangeRatesViewFxml);
    }

    public void switchToMorseCodeView() {
        switchToAnotherScene(pathToMorseCodeViewFxml);
    }

    public void switchToDeviceAdjustmentView() {
        switchToAnotherScene(pathToDeviceAdjustmentViewFxml);
    }

    public void switchToWeatherView() {
        switchToAnotherScene(pathToWeatherViewFxml);
    }

    public void switchToColorEffectsView() {
        switchToAnotherScene(pathToColorEffectsViewFxml);
    }

    private void switchToAnotherScene(String pathToView) {
        Stage stage = (Stage) nameDeviceLabel.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.switchToScene(pathToView, pathToCssFile);
    }
}
