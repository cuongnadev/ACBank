package com.example.javafx.Client;


import java.io.*;
import java.net.Socket;

public class SocketManager {
    private Socket clientSocket;
    private BufferedReader clientReader;
    private BufferedWriter clientWriter;

    public SocketManager() {}

    public void connect(String serverAddress, int serverPort) throws IOException {
        this.clientSocket = new Socket(serverAddress, serverPort);
        System.out.println("Connected successfully!");
        this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }


    public void sendMessage(String message){
        try{
            clientWriter.write(message);
            clientWriter.newLine();
            clientWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Sending file error in SocketManager class");
        }
    }

    public String receiverMessage() throws IOException {
        return clientReader.readLine();
    }


    public BufferedReader getClientReader() {
        return clientReader;
    }

    public BufferedWriter getClientWriter() {
        return clientWriter;
    }


}