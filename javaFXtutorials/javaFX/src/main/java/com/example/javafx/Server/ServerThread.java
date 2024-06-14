package com.example.javafx.Server;



import com.example.javafx.Client.ClientHandler;
import com.example.javafx.Models.Model;

import java.io.*;
import java.net.Socket;



public class ServerThread implements Runnable {
    private final Socket clientSocket;
    private String clientID;
    private BufferedReader serverReader;
    private BufferedWriter serverWriter;
    private boolean isClosed = false;





    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        isClosed = false;
    }

    public String getClientID() {
        return clientID;
    }

    @Override
    public void run() {
        try {
            // Open Read/Write stream on socket server.
            serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while (!isClosed) {
                String messageFromClient = serverReader.readLine();
                System.out.println("[Server Log] --> " + messageFromClient);
                if (messageFromClient == null) {
                    break;
                }
                handleClientMessage(messageFromClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
            isClosed = true;
            BankServer.serverThreadBus.removeServerThread(clientID);
            System.out.println("Client " + this.clientID + " disconnected.");
        } finally {
            close();
        }
    }

    private void handleClientMessage(String message) throws IOException {
        String[] messageSplit = message.split("_");
        if(messageSplit[0].equals("evaluateAccount")){
            String pAddress = messageSplit[1];
            System.out.println("[Server Log] --> " + pAddress);
            Model.getInstance().evaluateClientCred(pAddress);
            if(Model.getInstance().getClientLoginSuccessFlag()) {

                int Id = Model.getInstance().getCurrentClient().getId();

                this.clientID = String.valueOf(Id);
                String messageForm = "evaluateAccount_" + this.clientID + "_" + "success";
                System.out.println("Client " + this.clientID + " is active!");

                BankServer.serverThreadBus.notifyClient(this.clientID, messageForm);
            }
        } else if (messageSplit[0].equals("transferMoney")) {
            String senderId = messageSplit[1];
            String receiverID = messageSplit[2];
            double amount = Double.parseDouble(messageSplit[3]);
            String messageContent = messageSplit[4];

            // Perform money transfer logic (update balances, add transaction)
            boolean success = Model.getInstance().transferMoney(senderId, receiverID, amount, messageContent);
            if (success) {
                String senderMessage = "transferMoney_success_" + senderId + "_" + receiverID + "_" + amount;
                String receiverMessage = "receiveMoney_" + senderId + "_" + receiverID + "_" + amount;
                System.out.println("[Server Log] --> " + senderMessage);
                System.out.println("[Server Log] --> " + receiverMessage);

                // Notify both clients
                BankServer.serverThreadBus.notifyClient(senderId, senderMessage);

                BankServer.serverThreadBus.notifyClient(receiverID, receiverMessage);
            } else {
                BankServer.serverThreadBus.notifyClient(senderId, "transferMoney_failed");
            }
        }
    }

    // Utilities
    public void writeMessage(String message) throws IOException {
        serverWriter.write(message);
        serverWriter.newLine();
        serverWriter.flush();
    }

    public void close() {
        isClosed = true;
        try {
            if (serverReader != null) serverReader.close();
            if (serverWriter != null) serverWriter.close();
            if (clientSocket != null) clientSocket.close();

            // Loại bỏ luồng client khi kết thúc
            BankServer.serverThreadBus.removeServerThread(clientID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
