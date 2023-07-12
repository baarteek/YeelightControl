package com.example.yeelightcontrol.ui.utils;

import com.example.yeelightcontrol.api.YeelightAPI.YeelightActions;
import com.example.yeelightcontrol.api.YeelightAPI.YeelightBulb;

import java.util.concurrent.atomic.AtomicBoolean;

public class DeviceConnector {
    public static boolean connectAndSetName(YeelightBulb bulb) {
        YeelightActions action = new YeelightActions(bulb);

        AtomicBoolean result = new AtomicBoolean(true);
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() {
                try {
                    bulb.connect();
                    action.setName(bulb.getName());
                    bulb.disconnect();
                } catch (Exception e) {
                    result.set(false);
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result.get();
    }
}
