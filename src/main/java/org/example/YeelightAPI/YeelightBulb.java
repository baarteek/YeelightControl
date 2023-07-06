package org.example.YeelightAPI;

import java.io.*;
import java.net.*;

public class YeelightBulb {
    private String IP = "192.168.1.4";
    private final int PORT = 55443;
    private final int TIMEOUT = 5000;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;


    public void connect() throws IOException {
        socket = new Socket(IP, PORT);
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
        this.IP = ip;
    }

    public String getIP() {
        return IP;
    }
}