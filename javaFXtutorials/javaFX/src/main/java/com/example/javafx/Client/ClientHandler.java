package com.example.javafx.Client;

import com.example.javafx.Models.Clients;
import com.example.javafx.Models.Model;
import com.example.javafx.Server.BankServer;
import com.example.javafx.View.ClientMenuOptions;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;
    private BufferedReader clientReader;
    private BufferedWriter clientWriter;
    private boolean isClosed = false;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
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

                        // Show client window
                        Platform.runLater(() -> {
                            Model.getInstance().getViewFactory().showClientWindow(accountID);
                        });

                    } else if (messageSplit[0].equals("transferMoney")) {
                        if (messageSplit[1].equals("success")) {
                            Clients client = Model.getInstance().getDaoDriver().getClientsDao().getClientByID(Integer.parseInt(messageSplit[3]));

                            Platform.runLater(() -> {
                                showAlertSuccessful("Money transfer successful to " + client.getLastName() + " for $" + messageSplit[4]);
                                // refresh dashboard transfer
                                Model.getInstance().getViewFactory().getClientSession(messageSplit[2]).setSelectedMenuItem(ClientMenuOptions.REDASHBOARD);

                            });

                        } else if (messageSplit[1].equals("failed")) {
                            showAlertError("Money transfer failed.");
                        }

                    } else if (messageSplit[0].equals("receiveMoney")) {
                        Clients client = Model.getInstance().getDaoDriver().getClientsDao().getClientByID(Integer.parseInt(messageSplit[1]));
                        Platform.runLater(() -> {
                            showAlertSuccessful("You have received $" + messageSplit[3] + " from " + client.getLastName());
                            // refresh dashboard transfer
                            Model.getInstance().getViewFactory().getClientSession(messageSplit[2]).setSelectedMenuItem(ClientMenuOptions.REDASHBOARD);
                        });
                    } else {
                        showAlertError("Error, Evaluate account failed");
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

    public void close() {
        isClosed = true;
        try {
            if (clientReader != null) clientReader.close();
            if (clientSocket != null) clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlertError(String message) {
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
