package com.example.yeelightcontrol;

import com.example.yeelightcontrol.api.utils.DeviceData;
import com.example.yeelightcontrol.ui.utils.DialogHelper;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private final String pathToDeviceData = "src/main/resources/com/example/yeelightcontrol/devices/devices.txt";
    private DeviceData deviceData;
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
                mainLabel.setText("Select a device");
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


    private void addNewHBox(String name, String ip) {
        HBox deviceHBox = new HBox();
        deviceHBox.getStyleClass().add("addedDevices");

        Label nameLabel = new Label(name);
        Label ipLabel = new Label(ip);

        Button delButton = createDelButton(deviceHBox, name, ip);

        Region spacer1 = new Region();
        Region spacer2 = new Region();

        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        VBox.setMargin(deviceHBox, new Insets(0, 0, 5, 0));

        deviceHBox.getChildren().addAll(nameLabel, spacer1, ipLabel, spacer2, delButton);
        deviceHBox.setOnMouseClicked(event -> {
            System.out.println("clicked hbox");
        });
        devicesVBox.getChildren().add(deviceHBox);
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
    protected void addNewDevice() throws IOException {
        System.out.println("add new device");
    }
}