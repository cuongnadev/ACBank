package com.example.javafx.Controller.Admin;


import com.example.javafx.Client.ClientHandler;
import com.example.javafx.Models.*;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class ClientCellController implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;
    public Button accept_btn;

    private final Clients client;
    private ClientHandler clientHandler;
    private String clientId;

    public ClientCellController (Clients client){
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setClientData();
        delete_btn.setOnAction(event -> onDelete());
        accept_btn.setOnAction(event -> {
            try {
                acceptClient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



    public void setClientData() {
        fName_lbl.setText(client.getFirstName());
        lName_lbl.setText(client.getLastName());
        pAddress_lbl.setText(client.getPayeeAddress());
        date_lbl.setText(String.valueOf(client.getDateCreated()));
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        for (CheckingAccount checkingAccount : checkingAccountList) {
            if(client.getPayeeAddress().equals(checkingAccount.getOwner())) {
                ch_acc_lbl.setText(checkingAccount.getAccountNumber());
            }
        }
    }


    private void acceptClient() throws IOException {
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        for (Clients client : clientsList) {
            if(client.getPayeeAddress().equals(this.client.getPayeeAddress())) {
                clientId = String.valueOf(client.getId());
            }
        }

        new Thread(() -> {
            Platform.runLater(() -> {
                String payeeAddress = pAddress_lbl.getText().trim();

                //connect server
                try {
                    Socket socket = new Socket("localhost", 44105);
                    clientHandler = new ClientHandler(socket);
                    Model.getInstance().getClientExecutor().execute(clientHandler);
                    Model.getInstance().getClientHandlers().put(clientId, clientHandler);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String messageForm = "evaluateAccount_" + payeeAddress;

                System.out.println("[Client Log] --> " + messageForm);
                clientHandler.sendMessage(messageForm);
            });
        }).start();
    }


    public void onDelete () {
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        List<SavingAccount> savingAccountList = Model.getInstance().getDaoDriver().getSavingAccountDao().getAllSavingAccounts();
        List<Transaction> transactionList = Model.getInstance().getDaoDriver().getTransactionDao().getAllTransactions();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Client " + pAddress_lbl.getText() + "?");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {

            CountDownLatch latch = new CountDownLatch(3);

            new Thread(() -> {
                // Xóa SavingsAccounts
                for (SavingAccount savingAccount : savingAccountList) {
                    if (pAddress_lbl.getText().equals(savingAccount.getOwner())) {
                        Model.getInstance().getDaoDriver().getSavingAccountDao().deleteSavingAccount(savingAccount.getAccountNumber());
                        System.out.println(savingAccount.getAccountNumber());
                    }
                }
                latch.countDown();
            }).start();

            new Thread(() -> {
                // Xóa CheckingAccounts
                for (CheckingAccount checkingAccount : checkingAccountList) {
                    if (pAddress_lbl.getText().equals(checkingAccount.getOwner())) {
                        Model.getInstance().getDaoDriver().getCheckingAccountDao().deleteCheckingAccount(pAddress_lbl.getText());
                        break;
                    }
                }
                latch.countDown();
            }).start();

            new Thread(() -> {
                // Xóa Transactions
                for (Transaction transaction : transactionList) {
                    if (pAddress_lbl.getText().equals(transaction.getSender()) || pAddress_lbl.getText().equals(transaction.getReceiver())) {
                        Model.getInstance().getDaoDriver().getTransactionDao().deleteTransaction(pAddress_lbl.getText());
                        break;
                    }
                }
                latch.countDown();
            }).start();

            // Chờ cho đến khi tất cả các tác vụ xóa hoàn thành
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Xóa client
            for (Clients client : clientsList) {
                if (pAddress_lbl.getText().equals(client.getPayeeAddress())) {
                    Model.getInstance().getDaoDriver().getClientsDao().deleteClient(client.getId());
                    break;
                }
            }
            Model.getInstance().getViewFactory().getClientsController().refreshClientsListView();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();
        } else {
            return;
        }
    }

    private void showAlertErorr(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertSuccessful(String successfulMoneyTransfer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(successfulMoneyTransfer);
        alert.showAndWait();
    }
}
