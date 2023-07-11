package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    private final String pathToHelloViewFxml = "/com/example/yeelightcontrol/fxml/hello-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
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
    }

    public void backToHelloWorld() {
        Stage stage = (Stage) nameDeviceLabel.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.switchToScene(pathToHelloViewFxml, pathToCssFile);
    }

    public void powerToggleDeviceButtonClicked() {
        if(powerToggleButton.isSelected()) {
            powerToggleButton.setText("OFF");
        } else {
            powerToggleButton.setText("ON");
        }
    }

    public void powerToggleDeviceClicked() {
        powerToggleButton.fire();
    }
}
