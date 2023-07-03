package org.example;

import org.example.YeelightAPI.YeelightBulb;
import org.example.YeelightAPI.YeelightCommand;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb();

        try {
            bulb.connect("192.168.1.4");

            //YeelightCommand.setDuration(1000);
            //YeelightCommand.setTransitionEffect("sudden");

            //bulb.sendCommand(YeelightCommand.generateTurnOnCommand());
            //bulb.sendCommand(YeelightCommand.generateTurnOffCommand());
            //bulb.sendCommand(YeelightCommand.generateSetColorCommand(255, 0, 0));
            //bulb.sendCommand(YeelightCommand.generateSetBrightCommand(90));
            //bulb.sendCommand(YeelightCommand.generateSetColorTemperatureCommand(2000));
            //bulb.sendCommand(YeelightCommand.generateSetNameCommand("bulb"));
            bulb.sendCommand(YeelightCommand.generateSetAdjustCommand("increase", "color"));


            String response = bulb.readResponse();
            System.out.println(response);

            bulb.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}