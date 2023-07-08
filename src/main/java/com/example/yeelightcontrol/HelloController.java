package com.example.yeelightcontrol;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        YeelightBulb bulb = new YeelightBulb("192.168.1.4");
        YeelightActions actions = new YeelightActions(bulb);

        bulb.connect();
        actions.setColorRGB(0, 255, 0);
        bulb.disconnect();
    }
}