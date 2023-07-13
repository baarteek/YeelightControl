package com.example.yeelightcontrol.ui.controllers;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import com.example.yeelightcontrol.api.utils.DeviceData;
import com.example.yeelightcontrol.ui.utils.DeviceConnector;
import com.example.yeelightcontrol.ui.utils.DeviceInfo;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
import com.example.yeelightcontrol.ui.utils.SceneSwitcher;
import com.google.javascript.jscomp.jarjar.org.apache.tools.ant.Task;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloController implements Initializable {
    private final String pathToDeviceData = "src/main/resources/com/example/yeelightcontrol/devices/devices.txt";
    private final String pathToMainViewFxml = "/com/example/yeelightcontrol/fxml/main-view.fxml";
    private final String pathToCssFile = "/com/example/yeelightcontrol/css/style.css";
    private DeviceData deviceData;
    private Stage stage;
    @FXML
    private Label mainLabel;
    @FXML
    private VBox devicesVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            deviceData = new DeviceData(pathToDeviceData);
            List<String> devices = deviceData.readDeviceData();
            if(devices.isEmpty()) {
                setNoDeviceMessage();
            } else {
                setDeviceMessage();
                addDevicesToVBox(devices);
            }
        } catch (Exception e) {
            DialogHelper.showErrorDialog("Error", "Error reading device from file");
            Platform.exit();
        }
    }

    private void setNoDeviceMessage() {
        mainLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        mainLabel.setText("No devices saved. Click the plus to add a new device.");
    }

    private void setDeviceMessage() {
        mainLabel.setFont(Font.font("System", FontWeight.BOLD, 25));
        mainLabel.setText("Select a device");
    }

    private void addDevicesToVBox(List<String> devices) {
        for(String device : devices) {
            String[] data = extractNameAndIp(device);
            addNewHBox(data[0], data[1]);
        }
    }

    public String[] extractNameAndIp(String data) {
        String[] parts = data.split(", ");

        String namePart = parts[0];
        String ipPart = parts[1];

        String name = namePart.split(": ")[1];
        String ip = ipPart.split(": ")[1];

        return new String[]{name, ip};
    }

    private HBox createDeviceHBox() {
        HBox deviceHBox = new HBox();
        deviceHBox.getStyleClass().add("addedDevices");
        VBox.setMargin(deviceHBox, new Insets(0, 0, 5, 0));
        return deviceHBox;
    }

    private void addNewHBox(String name, String ip) {
        HBox deviceHBox = createDeviceHBox();

        Label nameLabel = createLabel(name);
        Label ipLabel = createLabel(ip);

        Button delButton = createDelButton(deviceHBox, name, ip);

        Region spacer1 = createSpacer();
        Region spacer2 = createSpacer();

        deviceHBox.getChildren().addAll(nameLabel, spacer1, ipLabel, spacer2, delButton);
        deviceHBox.setOnMouseClicked(event -> connectDevice(name, ip, nameLabel));

        addDeviceHBoxToView(deviceHBox);
    }

    private Label createLabel(String text) {
        return new Label(text);
    }

    private Region createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private void addDeviceHBoxToView(HBox deviceHBox) {
        devicesVBox.getChildren().add(deviceHBox);
    }

    private void connectDevice(String name, String ip, Label nameLabel) {
        YeelightBulb bulb = new YeelightBulb(name, ip);
        javafx.concurrent.Task<Boolean> connectionTask = new javafx.concurrent.Task<Boolean>()  {
            @Override
            protected Boolean call() throws Exception {
                Platform.runLater(DialogHelper::showProgressDialog);
                boolean result = DeviceConnector.connectAndSetName(bulb);
                Platform.runLater(DialogHelper::closeProgressDialog);
                return result;
            }
        };

        new Thread(connectionTask).start();

        connectionTask.setOnSucceeded(e -> {
            if(connectionTask.getValue()) {
                DeviceInfo.name = name;
                DeviceInfo.ip = ip;
                DeviceInfo.bulb = bulb;
                stage = (Stage) nameLabel.getScene().getWindow();
                SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
                sceneSwitcher.switchToScene(pathToMainViewFxml, pathToCssFile);

            }
        });

        connectionTask.setOnFailed(e -> {
            Platform.runLater(DialogHelper::closeProgressDialog);
            DialogHelper.showErrorDialog("Error", "An error occurred while trying to connect to the device. Please try again.");
        });
    }


    private Button createDelButton(HBox deviceHBox, String name, String ip) {
        Button delButton = new Button("DEL");
        delButton.getStyleClass().add("deleteDeviceButton");
        delButton.setOnAction(event -> {
            removeDeviceFromFile(name, ip);
            devicesVBox.getChildren().remove(deviceHBox);
            if(deviceData.readDeviceData().isEmpty()) {
                setNoDeviceMessage();
            }
        });
        return delButton;
    }

    private void removeDeviceFromFile(String name, String ip) {
        try {
            deviceData.removeDeviceData(name, ip);
        } catch (IOException e) {
            DialogHelper.showErrorDialog("Error", "Error removing device from file.");
        }
    }

    @FXML
    protected void addNewDevice() {
        Optional<Pair<String, String>> result = DialogHelper.showDeviceInputDialog();
        result.ifPresent(nameAndIp -> {
            String name = nameAndIp.getKey();
            String ip = nameAndIp.getValue();
            javafx.concurrent.Task<Boolean> connectTask = new javafx.concurrent.Task<Boolean>() {
                @Override
                protected Boolean call() {
                    return DeviceConnector.connectAndSetName(new YeelightBulb(name, ip));
                }
            };
            DialogHelper.showProgressDialog();
            new Thread(connectTask).start();

            connectTask.setOnSucceeded(e -> {
                DialogHelper.closeProgressDialog();

                boolean isConnected = connectTask.getValue();
                if(isConnected) {
                    setDeviceMessage();
                    deviceData.appendDeviceData(name, ip);
                    addNewHBox(name, ip);
                } else {
                    DialogHelper.showInformationDialog("Connection failed",
                            "Failed to connect to the device. Please check the device's IP address and try again.");
                }
            });

            connectTask.setOnFailed(e -> {
                DialogHelper.closeProgressDialog();

                DialogHelper.showInformationDialog("Error",
                        "An error occurred while trying to connect to the device. Please try again.");
            });
        });
    }
}