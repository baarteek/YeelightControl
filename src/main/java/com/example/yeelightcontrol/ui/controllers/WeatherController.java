package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.model.Weather;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class WeatherController implements Initializable {
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToSettingsViewFxml = "/com/example/yeelightcontrol/fxml/settings-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private Weather weather;
    private YeelightActions bulbActions;
    @FXML
    private Label nameTab;
    @FXML
    private HBox weatherHBox;
    @FXML
    private HBox temeratureHBox;
    @FXML
    private HBox windSpeedHBox;

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

    public void goToSettingsView() {
        disconnectWithDevice();
        switchToSettingsView();
    }

    private void switchToSettingsView() {
        Stage stage = (Stage) nameTab.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.switchToScene(pathToSettingsViewFxml, pathToCssFile);
    }

    public void setWeatherInfo() {
        Optional<Pair<String, String>> geographicCoordinates = DialogHelper.showWindowForEnteringCoordinate();
        if(geographicCoordinates.isPresent()) {
            double latitude = Double.parseDouble(geographicCoordinates.get().getKey());
            double longitude = Double.parseDouble(geographicCoordinates.get().getValue());
            tryGetWeather(latitude, longitude);
        }
    }

    private void tryGetWeather(double latitude, double longitude) {
        try {
            weather = new Weather(latitude, longitude);
            setFieldsToEnable();
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Weather Error", "An error occurred while trying to download weather information.");
        }
    }

    private void setFieldsToEnable() {
        weatherHBox.setDisable(false);
        temeratureHBox.setDisable(false);
        windSpeedHBox.setDisable(false);
    }

    public void setColorOfDeviceBasedOnWeather() {
        int weatherCode = (int) weather.getCode();
        bulbActions.changeLightBehaviorBasedOnWeatherCode(weatherCode);
        showWeatherInfo();
    }

    public void setColorOfDeviceBasedOnTemperature() {
        double temperature = weather.getTemperature();
        trySetColorOfDeviceBasedOnTemperature(temperature);
        showWeatherInfo();
    }

    private void trySetColorOfDeviceBasedOnTemperature(double temperature) {
        try {
            bulbActions.setColorBasedOnTemperature(temperature);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Temperature Error", "An error occurred while trying to change the color of the device based on temperature.");
        }
    }

    public void setColorOfDeviceBasedOnWindSpeed() {
        double windSpeed = weather.getWindSpeed();
        trySetColorOfDeviceBasedOnWindSpeed(windSpeed);
        showWeatherInfo();
    }

    private void trySetColorOfDeviceBasedOnWindSpeed(double windSpeed) {
        try {
            bulbActions.changeBulbColorBasedOnWindSpeed(windSpeed);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Wind Speed Error", "An error occurred while trying to change the color of the device based on the wind speed.");
        }
    }

    private void showWeatherInfo() {
        String weatherDescription = weather.getWeatherDescription();
        String message = "Weather: " + weatherDescription + "\nTemperature: " + weather.getTemperature() + " °C\nWind Speed: " + weather.getWindSpeed() + " km/h\nWind Direction: " + weather.getWindDirection();
        DialogHelper.showInformationDialog("Weather", message);
    }
}
