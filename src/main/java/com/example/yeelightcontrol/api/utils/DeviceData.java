package com.example.yeelightcontrol.api.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DeviceData {
    private String filePath;

    public DeviceData(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
    }

    private void createFileIfNotExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }

    public void appendDeviceData(String deviceName, String deviceIp) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("Name: " + deviceName + ", IP: " + deviceIp);
        } catch (IOException e) {
            throw new RuntimeException();
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
            throw new RuntimeException();
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
