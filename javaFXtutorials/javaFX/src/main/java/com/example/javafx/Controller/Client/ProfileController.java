package com.example.javafx.Controller.Client;

import com.example.javafx.Models.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {


    public Label firstName_lbl;
    public Label lastName_lbl;
    public Label password_lbl;
    public Label pAddress_lbl;
    public TextField fName_fld;
    public TextField lName_fld;
    public TextField password_fld;
    public Button edit_btn;
    public Label date_lbl;
    public Label ch_acc_num_lbl;
    public Label bal_lbl;
    public Label num_of_sav_lbl;
    public Label income_lbl;
    public Label expense_lbl;
    private String clientId;
    String pAddress = "";
    String password = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        edit_btn.setOnAction(event -> onEdit());
        Model.getInstance().getViewFactory().setProfileController(this);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
        if(clientId != null) setdataLabel();
    }
    public void setdataLabel() {
        int Id = Integer.parseInt(clientId);
        String pass = Model.getInstance().getClients().getPassword();
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        List<SavingAccount> savingAccountList = Model.getInstance().getDaoDriver().getSavingAccountDao().getAllSavingAccounts();
        List<Transaction> transactionList = Model.getInstance().getDaoDriver().getTransactionDao().getAllTransactions();
        double income = 0;
        double expense = 0;
        for (Clients client : clientsList){
            if(Id == client.getId()){
                pAddress = client.getPayeeAddress();
                password = client.getPassword();
                firstName_lbl.setText(client.getFirstName());
                lastName_lbl.setText(client.getLastName());
                password_lbl.setText(pass);
                pAddress_lbl.setText(client.getPayeeAddress());
                date_lbl.setText(client.getDateCreated());
                break;
            }
        }
        for (CheckingAccount checkingAccount : checkingAccountList){
            if (pAddress.equals(checkingAccount.getOwner())){
                ch_acc_num_lbl.setText(checkingAccount.getAccountNumber());
                bal_lbl.setText(String.valueOf(checkingAccount.getBalance()));
            }
        }
        int count = 0;
        for (SavingAccount savingAccount : savingAccountList){
            if (pAddress.equals(savingAccount.getOwner())){
                count = count + 1;
            }
        }
        num_of_sav_lbl.setText(String.valueOf(count));

        for (Transaction transaction : transactionList){
            if (pAddress.equals(transaction.getSender())){
                expense += transaction.getAmount();
            } else if (pAddress.equals(transaction.getReceiver())){
                income += transaction.getAmount();
            }
        }
        income_lbl.setText(String.valueOf(income));
        expense_lbl.setText(String.valueOf(expense));
        fName_fld.setText("");
        lName_fld.setText("");
        password_fld.setText("");
    }

    private void onEdit() {
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        for (Clients client : clientsList){
            if(pAddress.equals(client.getPayeeAddress())){
                if (!(fName_fld.getText().trim().isEmpty())){
                    client.setFirstName(fName_fld.getText());
                    Model.getInstance().getDaoDriver().getClientsDao().updateClient(client);
                }
                if (!(lName_fld.getText().trim().isEmpty())){
                    client.setLastName(lName_fld.getText());
                    Model.getInstance().getDaoDriver().getClientsDao().updateClient(client);
                }
                if (!(password_fld.getText().trim().isEmpty())){
                    String pass = Model.HashPassword(password_fld.getText());
                    client.setPassword(pass);
                    Model.getInstance().getDaoDriver().getClientsDao().updateClient(client);
                }
                setdataLabel();
            }
        }
    }
}
