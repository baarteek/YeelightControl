package org.example;

import org.example.YeelightAPI.YeelightBulb;
import org.example.YeelightAPI.YeelightCommand;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        YeelightBulb bulb = new YeelightBulb();

        try {
            bulb.connect("192.168.1.4");

            //bulb.sendCommand(YeelightCommand.generateTurnOnCommand());
            //bulb.sendCommand(YeelightCommand.generateTurnOffCommand());
            bulb.sendCommand(YeelightCommand.generateSetColorCommand(100, 0, 10));

            String response = bulb.readResponse();
            System.out.println(response);

            bulb.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}