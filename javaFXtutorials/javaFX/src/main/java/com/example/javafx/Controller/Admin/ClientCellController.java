package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;
    public Button accept_btn;

    private final Clients client;

    public ClientCellController (Clients client){
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setClientData();
        delete_btn.setOnAction(event -> onDelete());
        accept_btn.setOnAction(event -> acceptClient());
    }



    public void setClientData(){
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


    private void acceptClient() {
        String payeeAddress = pAddress_lbl.getText().trim();
        Stage stage = (Stage) pAddress_lbl.getScene().getWindow();
        String password = null;
        // Tạo một hộp thoại đầu vào để nhập mật khẩu

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Password");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your password:");

        // Hiển thị hộp thoại và chờ người dùng nhập mật khẩu
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            password = result.get().trim();
            Model.getInstance().evaluateClientCred(payeeAddress, password);
            if (Model.getInstance().getClientLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showClientWindow();

                //Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Wrong password, cannot access account");
                alert.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("User canceled the password input.");
            alert.showAndWait();
        }
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
            // Xóa client
            for (Clients client : clientsList) {
                if (pAddress_lbl.getText().equals(client.getPayeeAddress())) {
                    Model.getInstance().getDaoDriver().getClientsDao().deleteClient(client.getId());
                }
            }
            // Xóa CheckingAccounts
            for (CheckingAccount checkingAccount : checkingAccountList) {
                if (pAddress_lbl.getText().equals(checkingAccount.getOwner())) {
                    Model.getInstance().getDaoDriver().getCheckingAccountDao().deleteCheckingAccount(pAddress_lbl.getText());
                }
            }
            // Xóa SavingsAccounts
            for (SavingAccount savingAccount : savingAccountList) {
                if (pAddress_lbl.getText().equals(savingAccount.getOwner())) {
                    Model.getInstance().getDaoDriver().getSavingAccountDao().deleteSavingAccount(pAddress_lbl.getText());
                }
            }
            // Xóa transaction
            for (Transaction transaction : transactionList) {
                if (pAddress_lbl.getText().equals(transaction.getSender()) || pAddress_lbl.getText().equals(transaction.getReceiver())) {
                    Model.getInstance().getDaoDriver().getTransactionDao().deleteTransaction(pAddress_lbl.getText());
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
}
