package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.retrievers.ExchangeRateRetriever;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExchangeRatesController implements Initializable {
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private YeelightActions bulbActions;
    @FXML
    private Label nameTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTab.setText("Exchanges Rates");

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

    public void dollarExchangeRateClicked() {
        trySetColorBasedOnDollarRate();
        showDollarExchangeRate();
    }

    private void trySetColorBasedOnDollarRate() {
        try {
            bulbActions.setColorBasedOnDollarRate();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error: Dollar Exchange Rate", "Error with changing device settings or downloading the current dollar exchange rate.");
        }
    }

    private void showDollarExchangeRate() {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double dollarRate = rateRetriever.getDollarExchangeRate();
        String message = "Today's dollar exchange rate is " + dollarRate + " PLN.\n\nThe color of the device has been changed based on the dollar rate.";
        DialogHelper.showInformationDialog("Dollar Exchange Rate", message);
    }

    public void euroExchangeRateClicked() {
        trySetColorBasedOnEuroRate();
        showEuroExchangeRate();
    }

    private void trySetColorBasedOnEuroRate() {
        try {
            bulbActions.setColorBasedOnEuroRate();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error: Euro Exchange Rate", "Error with changing device settings or downloading the current Euro exchange rate.");
        }
    }

    private void showEuroExchangeRate() {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double euroRate = rateRetriever.getEuroExchangeRate();
        String message = "Today's Euro exchange rate is " + euroRate + " PLN.\n\nThe color of the device has been changed based on the Euro rate.";
        DialogHelper.showInformationDialog("Euro Exchange Rate", message);
    }

    public void britishPoundExchangeRateClicked() {
        trySetColorBasedOnBritishPoundRate();
        showBritishPoundExchangeRate();
    }

    private void trySetColorBasedOnBritishPoundRate() {
        try {
            bulbActions.setColorBasedOnBritishPoundRate();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error: British Pound Exchange Rate", "Error with changing device settings or downloading the current british pound exchange rate.");
        }
    }

    private void showBritishPoundExchangeRate() {
        ExchangeRateRetriever rateRetriever = new ExchangeRateRetriever();
        double britishPoundRate = rateRetriever.getBritishPoundExchangeRate();
        String message = "Today's british pound exchange rate is " + britishPoundRate + " PLN.\n\nThe color of the device has been changed based on the British Pound rate.";
        DialogHelper.showInformationDialog("British Pound Exchange Rate", message);
    }
}
