package com.example.javafx.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String Id;

    public ClientSocket(String serverAddress, int serverPort, String Id) throws IOException {
        this.Id = Id;
        this.socket = new Socket(serverAddress, serverPort);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void start() {
        new Thread(() -> {
            try {
                // Gửi ID khách hàng (địa chỉ thanh toán) tới máy chủ
                out.println(Id);

                // Lắng nghe phản hồi từ máy chủ
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println("Server: " + serverResponse);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
