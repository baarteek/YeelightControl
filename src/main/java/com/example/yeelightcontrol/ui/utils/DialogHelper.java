package com.example.yeelightcontrol.ui.utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class DialogHelper {
    private static final String pathToCssFile = "/com/example/yeelightcontrol/css/dialog.css";
    private static Stage dialogStage;

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

    public static Optional<Pair<String, String>> showWindowForEnteringCoordinate() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.getDialogPane().getStylesheets().add(DialogHelper.class.getResource(pathToCssFile).toExternalForm());
        dialog.setTitle("Enter geographical coordinates");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField latitudeField = new TextField();
        latitudeField.setPromptText("Latitude: ");
        TextField longitudeField = new TextField();
        longitudeField.setPromptText("Longitude: ");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(new Label("Latitude:"), latitudeField, new Label("Longitude:"), longitudeField);
        vbox.getStyleClass().add("addedDevices");
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if(DataValidator.areCoordinatesValid(latitudeField.getText(), longitudeField.getText())) {
                    return new Pair<>(latitudeField.getText(), longitudeField.getText());
                } else {
                    showInformationDialog("Incorrect data", "Invalid geographic coordinates entered.");
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static void showProgressDialog() {
        Platform.runLater(() -> {
            dialogStage = new Stage();
            dialogStage.setMinWidth(150);
            dialogStage.setTitle("Connecting...");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setOnCloseRequest(event -> event.consume());

            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.getStyleClass().add("dialog-progress-indicator");
            progressIndicator.setProgress(-1f);

            VBox vBox = new VBox();
            vBox.getStyleClass().add("dialog-vbox");
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(15));
            vBox.getChildren().add(progressIndicator);

            Scene scene = new Scene(vBox);
            scene.getStylesheets().add(DialogHelper.class.getResource(pathToCssFile).toExternalForm());
            dialogStage.setScene(scene);

            dialogStage.show();
        });
    }

    public static void closeProgressDialog() {
        Platform.runLater(() -> {
            if (dialogStage != null) {
                dialogStage.close();
            }
        });
    }
}
