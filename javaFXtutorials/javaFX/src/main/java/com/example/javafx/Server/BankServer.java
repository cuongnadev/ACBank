package com.example.javafx.Server;

import com.example.javafx.Client.ClientHandler;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class BankServer {
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, ClientHandler> clients;

    public BankServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ConcurrentHashMap<>();
    }

    public void start() {
        System.out.println("Server started...");
        while (true) {
            try {
                new ClientHandler(serverSocket.accept(), this).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void notifyClient(String clientId, String message) {
        ClientHandler clientHandler = clients.get(clientId);
        if (clientHandler != null) {
            clientHandler.sendMessage(message);
        }
    }

    public synchronized void registerClient(String clientId, ClientHandler clientHandler) {
        clients.put(clientId, clientHandler);
    }

    public synchronized void removeClient(String clientId) {
        clients.remove(clientId);
    }

    public synchronized boolean isClientConnected(String clientId) {
        return clients.containsKey(clientId);
    }


}
