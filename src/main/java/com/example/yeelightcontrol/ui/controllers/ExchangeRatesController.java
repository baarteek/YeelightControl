package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ExchangeRatesController implements Initializable {
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    @FXML
    private Label nameTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTab.setText("Exchanges Rates");
    }

    public void backToMainView() {
        Stage stage = (Stage) nameTab.getScene().getWindow();
        SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        sceneSwitcher.switchToScene(pathToMainViewFxml, pathToCssFile);
    }
}
