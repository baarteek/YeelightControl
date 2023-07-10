package com.example.yeelightcontrol.ui.utils;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Optional;

public class DialogHelper {
    private static final String pathToCssFile = "/com/example/yeelightcontrol/css/dialog.css";

    public static void showInformationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(DialogHelper.class.getResource(pathToCssFile).toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getDialogPane().getStylesheets().add(DialogHelper.class.getResource(pathToCssFile).toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().getStylesheets().add(DialogHelper.class.getResource(pathToCssFile).toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(DialogHelper.class.getResource(pathToCssFile).toExternalForm());
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }

    public static Optional<Pair<String, String>> showDeviceInputDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(DialogHelper.class.getResource(pathToCssFile).toExternalForm());
        dialog.setTitle("Device Input Dialog");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField ipField = new TextField();
        ipField.setPromptText("IP");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(new Label("Name:"), nameField, new Label("IP:"), ipField);
        vbox.getStyleClass().add("addedDevices");
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if(DataValidator.isIPValid(ipField.getText()) && DataValidator.isNameValid(nameField.getText())) {
                    return new Pair<>(nameField.getText(), ipField.getText());
                } else {
                    showInformationDialog("Incorrect data", "Invalid name or IP address entered.");
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
