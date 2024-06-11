package com.example.javafx.Client;

import com.example.javafx.Models.Clients;
import com.example.javafx.Models.Model;
import com.example.javafx.Server.BankServer;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private String Id;
    private final Socket clientSocket;
    private BufferedReader clientReader;
    private BufferedWriter clientWriter;
    private boolean isClosed = false;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        isClosed = false;
        // Open Read/Write stream on socket server.
        this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    @Override
    public void run() {
            try {
                while (!isClosed) {
                    String messageResponse = clientReader.readLine();
                    System.out.println("[Client Log] --> " + messageResponse);
                    if (messageResponse != null) {
                        String[] messageSplit = messageResponse.split("_");
                        if (messageSplit[2].equals("success")) {
                            System.out.println(messageResponse);
                            String accountID = messageSplit[1];

                            // add online client to clientList
                            Clients client = Model.getInstance().getDaoDriver().getClientsDao().getClientByID(Integer.parseInt(accountID));
                            Model.getInstance().getOnlineClientList().add(client);
                            Platform.runLater(() -> {
                                // Show main chat window
                                Model.getInstance().getViewFactory().showClientWindow(accountID);
                            });

                        } else if (messageSplit[0].equals("transferMoney")) {
                            if (messageSplit[1].equals("success")) {
                                Model.getInstance().getViewFactory().getDashboardController().refreshDataTransaction();
                                Model.getInstance().getViewFactory().getDashboardController().setDataLabel();
                                showAlertSuccessful("Money transfer successful to " + messageSplit[2] + " for $" + messageSplit[3]);
                            } else if (messageSplit[1].equals("failed")) {
                                showAlertErorr("Money transfer failed.");
                            }

                        } else if (messageSplit[0].equals("receiveMoney")) {
                            Model.getInstance().getViewFactory().getDashboardController().refreshDataTransaction();
                            Model.getInstance().getViewFactory().getDashboardController().setDataLabel();
                            showAlertSuccessful("You have received $" + messageSplit[2] + " from " + messageSplit[1]);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("confirmation Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Lỗi, giá tài khoản thất bại.");
                            alert.showAndWait();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                isClosed = true;
            } finally {
                close();
            }
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

    public void close() {
        isClosed = true;
        try {
            if (clientReader != null) clientReader.close();
            if (clientSocket != null) clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlertErorr(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    private void showAlertSuccessful(String successfulMoneyTransfer) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(successfulMoneyTransfer);
            alert.showAndWait();
        });
    }
}
