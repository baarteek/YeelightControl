package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.utils.DeviceData;
import com.example.yeelightcontrol.ui.utils.DataValidator;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private final String pathToDeviceData = "src/main/resources/com/example/yeelightcontrol/devices/devices.txt";
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private YeelightBulb bulb;
    private YeelightActions bulbActions;
    @FXML
    private Label nameTab;
    @FXML
    private TextField newNameTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTab.setText("Settings");
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

    public void renameDevice() {
        String newName = newNameTextField.getText();
        if(DataValidator.isNameValid(newName)) {
            tryRenameDevice(newName);
            replaceDeviceNameInFile(newName);
            DeviceInfo.name = newName;
            DialogHelper.showInformationDialog("Rename Device", "Device renamed successfully.");
        } else {
            DialogHelper.showInformationDialog("Invalid Name", "The given name is invalid.");
        }
    }

    private void tryRenameDevice(String newName) {
        try {
            bulbActions.setName(newName);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Rename Device Error", "An error occurred while trying to rename the device.");
        }
    }

    private void replaceDeviceNameInFile(String newName) {
        String oldName = bulb.getName();
        String ip = bulb.getIP();
        tryRemoveDeviceData(oldName, ip);
        appendDeviceData(newName);
    }

    private void tryRemoveDeviceData(String name, String ip) {
        DeviceData deviceData = new DeviceData(pathToDeviceData);
        try {
            deviceData.removeDeviceData(name, ip);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Remove Device Data Error", "An error occurred while trying to remove device data.");
        }
    }

    private void appendDeviceData(String newName) {
        String ip = bulb.getIP();
        DeviceData deviceData = new DeviceData(pathToDeviceData);
        deviceData.appendDeviceData(newName, ip);
    }

    public void showDeviceInformation() {
        String deviceInfoJson = getDeviceInfo();
        if(deviceInfoJson.isEmpty()) {
            DialogHelper.showErrorDialog("Device Info Error", "No device information has been retrieved.");
        } else {
            String deviceInfo = buildInfoMessage(deviceInfoJson);
            DialogHelper.showInformationDialog("Device Information", deviceInfo);
        }
    }

    private String getDeviceInfo() {
        try {
            return bulbActions.getInfo("power", "bright", "color_mode", "rgb", "ct", "hue", "sat");
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Get Device Info Error", "An error occurred while trying to get device info.");
        }
        return "";
    }

    private String buildInfoMessage(String info) {
        JSONObject jsonObject = new JSONObject(info);
        JSONArray resultArray = jsonObject.getJSONArray("result");
        StringBuilder infoBuilder = new StringBuilder();

        infoBuilder.append("Power: ").append(resultArray.getString(0)).append("\n");
        infoBuilder.append("Bright: ").append(resultArray.getString(1)).append("\n");
        infoBuilder.append("Color Mode: ").append(resultArray.getString(2)).append("\n");
        infoBuilder.append("RGB: ").append(resultArray.getString(3)).append("\n");
        infoBuilder.append("CT: ").append(resultArray.getString(4)).append("\n");
        infoBuilder.append("Hue: ").append(resultArray.getString(5)).append("\n");
        infoBuilder.append("Sat: ").append(resultArray.getString(6)).append("\n");

        return infoBuilder.toString();
    }
}
