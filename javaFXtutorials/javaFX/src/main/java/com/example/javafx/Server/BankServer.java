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
    private boolean running = false;


    public BankServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverThreadBus = new ServerThreadBus();

        // Khởi tạo ThreadPoolExecutor cho server threads
        serverExecutor = new ThreadPoolExecutor(10, 50, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(8));
    }



    //start server
    public void start() {
        running = true;
        System.out.println("Server started...");
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                if (!running) {
                    break;
                }
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThreadBus.addServerThread(serverThread);
                System.out.println("Number of running threads : " + serverThreadBus.getServerThreadListSize());
                serverExecutor.execute(serverThread);
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                } else {
                    System.out.println("Server stopped.");
                }
            }
        }
        // Shutdown the executor gracefully
        serverExecutor.shutdown();
        try {
            if (!serverExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                serverExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            serverExecutor.shutdownNow();
        }
    }


    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
