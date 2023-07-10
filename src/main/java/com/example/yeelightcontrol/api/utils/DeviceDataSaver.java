package com.example.yeelightcontrol.api.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DeviceDataSaver {

    private String filePath;

    public DeviceDataSaver(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appendDeviceData(String deviceName, String deviceIp) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(deviceName + "," + deviceIp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readDeviceData() {
        List<String> devices = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                devices.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devices;
    }

    public void removeDeviceData(String name, String ip) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        String lineToRemove = "Name: " + name + ", IP: " + ip;

        lines.remove(lineToRemove);

        Files.write(Paths.get(filePath), lines);
    }
}