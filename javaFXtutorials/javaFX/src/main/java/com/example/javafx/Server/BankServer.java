package com.example.javafx.Server;


import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BankServer {
    public static volatile ServerThreadBus serverThreadBus;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor serverExecutor;



    public BankServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverThreadBus = new ServerThreadBus();

        // Khởi tạo ThreadPoolExecutor cho server threads
        serverExecutor = new ThreadPoolExecutor(10, 50, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(8));
    }

    //start server
    public void start() {
        System.out.println("Server started...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThreadBus.addServerThread(serverThread);
                System.out.println("Number of running threads : " + serverThreadBus.getServerThreadListSize());
                serverExecutor.execute(serverThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
