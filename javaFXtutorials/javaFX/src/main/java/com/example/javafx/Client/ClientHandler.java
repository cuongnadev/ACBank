package com.example.javafx.Client;

import com.example.javafx.Models.Model;
import com.example.javafx.Server.BankServer;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BankServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientId;
    private ClientSession clientSession;

    public ClientHandler(Socket socket, BankServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Đọc ID khách hàng từ client
            clientId = in.readLine();
            clientSession = new ClientSession(clientId);
            System.out.println("Client ID received: " + clientId);

            // Đăng ký client vào server
            synchronized (server) {
                server.registerClient(clientId, this);
                System.out.println("Client registered: " + clientId);
                System.out.println("Is client connected: " + server.isClientConnected(clientId));
            }

            Platform.runLater(() -> {
                Model.getInstance().getViewFactory().showClientWindow(clientId);
            });

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from " + clientId + ": " + inputLine);
                if (inputLine.startsWith("TRANSACTION")) {
                    synchronized (server) {
                        server.notifyClient(clientId, "Transaction processed: " + inputLine);
                    }
                }
            }

            // Xử lý khi client ngắt kết nối
            in.close();
            out.close();
            clientSocket.close();
            synchronized (server) {
                server.removeClient(clientId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getClientId() {
        return clientId;
    }
}
