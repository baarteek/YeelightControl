package com.example.yeelightcontrol.api.YeelightAPI;

import java.io.*;
import java.net.*;

public class YeelightBulb {
    private final int PORT = 55443;
    private final String name;
    private final String ip;
    private final int TIMEOUT = 1000;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public YeelightBulb(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public synchronized void connect() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(ip, PORT);
            socket.setSoTimeout(TIMEOUT);
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        }
    }

    public synchronized void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            reader.close();
            writer.close();
            socket = null;
            reader = null;
            writer = null;
        }
    }

    public synchronized void sendCommand(String command) throws IOException {
        try {
            writer.write(command);
            writer.flush();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public synchronized String readResponse() throws IOException {
        return reader.readLine();
    }

    public String getIP() {
        return ip;
    }

    public String getName() {
        return name;
    }
}