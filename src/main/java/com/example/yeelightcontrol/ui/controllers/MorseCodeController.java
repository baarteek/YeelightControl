package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.utils.MorseCodeTranslator;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MorseCodeController implements Initializable {
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private YeelightActions bulbActions;
    @FXML
    private Label nameTab;
    @FXML
    private Slider durationSlider;
    @FXML
    private Label durationLabel;
    @FXML
    private TextField displayMorseCodeTextField;
    @FXML
    private TextField translateToCodeMorseTextField;
    @FXML
    private TextField translateToLettersTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTab.setText("Morse Code");
        connectToDevice();
        bindLabelToSlider();
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

    private void bindLabelToSlider() {
        durationLabel.setText(String.valueOf((int) durationSlider.getValue()));
        durationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            durationLabel.setText(String.valueOf(newValue.intValue()) + " ms");
        });
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

    public void displayMorseCodeOnDevice() {
        String message = displayMorseCodeTextField.getText();
        if(message.isEmpty()) {
            DialogHelper.showWarningDialog("Empty Text Field", "Fill in the field of the message that will be translated into morse code.");
        } else {
            int duration = (int) durationSlider.getValue();
            String morseCode = MorseCodeTranslator.textToMorseCode(message);
            tryDisplayMorseCodeOnDevice(morseCode, duration);
        }
    }

    private void tryDisplayMorseCodeOnDevice(String morseCode, int duration) {
        try {
            bulbActions.morseCodeSignal(morseCode, duration);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error: Morse Code", "Error trying to display morse code on device.");
        }
    }

    public void translateToMorseCode() {
        String text = translateToCodeMorseTextField.getText();
        if(text.isEmpty()) {
            DialogHelper.showWarningDialog("Empty Text Field", "Fill in the field of the message that will be translated into morse code.");
        } else {
            showTranslatedTextToMorseCode(text);
        }
    }

    private void showTranslatedTextToMorseCode(String text) {
        String morseCode = MorseCodeTranslator.textToMorseCode(text);
        String message = "Translated text:\n\n" + morseCode;
        DialogHelper.showInformationDialog("Morse Code", message);
    }

    public void translateMorseCodeToLetters() {
        String morseCode = translateToLettersTextField.getText();
        if(morseCode.isEmpty()) {
            DialogHelper.showWarningDialog("Empty Text Field", "Fill in the field of the morse code that will be translated into letters.");
        } else {
            showTranslatedMorseCodeToLetters(morseCode);
        }
    }

    private void showTranslatedMorseCodeToLetters(String morseCode) {
        String text = MorseCodeTranslator.morseCodeToText(morseCode);
        String message = "Translated text:\n\n" + text;
        DialogHelper.showInformationDialog("Morse Code", message);
    }
}
