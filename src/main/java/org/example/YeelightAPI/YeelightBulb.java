package org.example.YeelightAPI;

import java.io.*;
import java.net.*;

public class YeelightBulb {
    private String ip;
    private static final int PORT = 55443;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;


    public void connect(String ip) throws IOException {
        this.socket = new Socket(ip, PORT);
        this.socket.setSoTimeout(5000);
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    public void disconnect() throws IOException {
        this.socket.close();
    }

    public void sendCommand(String command) throws IOException {
        this.writer.write(command);
        this.writer.flush();
    }

    public String readResponse() throws IOException {
        return this.reader.readLine();
    }
}