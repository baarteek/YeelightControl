package com.example.yeelightcontrol.api.YeelightAPI;

import java.io.*;
import java.net.*;

public class YeelightBulb {
    private final int PORT = 55443;
    private String name;
    private String ip;
    private final int TIMEOUT = 5000;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public YeelightBulb(String name, String ip) {
        this.name = name;
        this.ip = ip;
    }

    public void connect() throws IOException {
        socket = new Socket(ip, PORT);
        socket.setSoTimeout(TIMEOUT);
        reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public void sendCommand(String command) throws IOException {
        try {
            writer.write(command);
            writer.flush();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public String readResponse() throws IOException {
        return reader.readLine();
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getIP() {
        return ip;
    }

    public String getName() {
        return name;
    }
}