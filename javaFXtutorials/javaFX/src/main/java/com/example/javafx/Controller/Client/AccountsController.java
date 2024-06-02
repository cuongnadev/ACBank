package com.example.javafx.Controller.Client;

import com.example.javafx.Models.*;
import com.example.javafx.View.SavingCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;
    public TextField sav_acc_num_fld;
    public Button search_btn;
    public ListView<SavingAccount> savings_listview;
    private String clientId;
    String pAddress = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trans_to_ch_btn.setOnAction(event -> onTransToCH());
        search_btn.setOnAction(event -> onSearch());
        Model.getInstance().getViewFactory().setAccountsController(this);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
        if(clientId != null){
            refreshDataLabel();
            List<SavingAccount> savingAccounts = getSavingOfSQLite();
            savings_listview.getItems().addAll(savingAccounts);
            savings_listview.setCellFactory(listView -> new SavingCellFactory());

        }
    }

    private List<SavingAccount> getSavingOfSQLite() {
        savings_listview.getItems().clear();
        int Id = Integer.parseInt(clientId);
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        List<SavingAccount> savingAccountList = Model.getInstance().getDaoDriver().getSavingAccountDao().getAllSavingAccounts();
        for(Clients client : clientsList) {
            if(Id == client.getId()) {
                pAddress = client.getPayeeAddress();
                break;
            }
        }

        List<SavingAccount> savingAccounts = new ArrayList<>();
        for (SavingAccount savingAccount : savingAccountList) {
            if(pAddress.equals(savingAccount.getOwner())){
                SavingAccount newSavingAccount = new SavingAccount(
                        savingAccount.getOwner(),
                        savingAccount.getAccountNumber(),
                        savingAccount.getBalance(),
                        savingAccount.getWithdrawalLimit());

                savingAccounts.add(newSavingAccount);
            }
        }
        savingAccounts.sort((t1, t2) -> (String.valueOf(t2.getBalance()).compareTo(String.valueOf(t1.getBalance()))));
        return savingAccounts;
    }

    public void onSearch(){
        List<SavingAccount> savingAccountList = Model.getInstance().getDaoDriver().getSavingAccountDao().getAllSavingAccounts();
        String Sav_num = sav_acc_num_fld.getText();
        Boolean check = false;
        savings_listview.getItems().clear();

        for (SavingAccount savingAccount : savingAccountList) {
            if (Sav_num.equals(savingAccount.getAccountNumber())){
                check = true;
                SavingAccount newSavingAccount = new SavingAccount(
                        savingAccount.getOwner(),
                        savingAccount.getAccountNumber(),
                        savingAccount.getBalance(),
                        savingAccount.getWithdrawalLimit());

                savings_listview.getItems().add(newSavingAccount);
                savings_listview.setCellFactory(listView -> new SavingCellFactory());
            }
        }
        if (!check){
            showAlert("Error! Enter payee address no valid.");
            sav_acc_num_fld.setText("");
            List<SavingAccount> savingAccounts = getSavingOfSQLite();
            savings_listview.getItems().addAll(savingAccounts);
            savings_listview.setCellFactory(listView -> new SavingCellFactory());
        }
    }

    public void refreshDataLabel(){
        int Id = Integer.parseInt(clientId);
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        for (Clients client : clientsList) {
            if (Id == client.getId()){
                ch_acc_date.setText(client.getDateCreated());
                pAddress = client.getPayeeAddress();
                break;
            }

        }
        for (CheckingAccount checkingAccount : checkingAccountList){
            if (pAddress.equals(checkingAccount.getOwner())){
                ch_acc_num.setText(checkingAccount.getAccountNumber());
                ch_acc_bal.setText(String.valueOf(checkingAccount.getBalance()));
                transaction_limit.setText(String.valueOf(checkingAccount.getTransactionLimit()));
                break;
            }
        }
        sav_acc_num_fld.setText("");
        amount_to_ch.setText("");
        List<SavingAccount> savingAccounts = getSavingOfSQLite();
        savings_listview.getItems().setAll(savingAccounts);
        savings_listview.setCellFactory(listView -> new SavingCellFactory());
    }



    private void onTransToCH(){
        int Id = Integer.parseInt(clientId);
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        String Sav_num = sav_acc_num_fld.getText();
        List<SavingAccount> savingAccountList = Model.getInstance().getDaoDriver().getSavingAccountDao().getAllSavingAccounts();
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        for (Clients client : clientsList) {
            if (Id == client.getId()){
                pAddress = client.getPayeeAddress();
                break;
            }
        }

        double amount;
        try {
            amount = Double.parseDouble(amount_to_ch.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Invalid amount. Please enter a valid number.");
            return;
        }
        // Kiểm tra dữ liệu
        if (amount <= 0){
            showAlert("Please enter valid Amount.");
        }
        //Truy xuất balance từ tài khoản saving
        for (SavingAccount savingAccount : savingAccountList){
            if(Sav_num.equals(savingAccount.getAccountNumber())){
                double amountSV = savingAccount.getBalance();
                if (amountSV < amount){
                    showAlert("Insufficient funds. Please check your balance.");
                    return;
                }
                savingAccount.setBalance(amountSV - amount);
                //Update lại tiền Saving
                Model.getInstance().getDaoDriver().getSavingAccountDao().updateSavingAccount(savingAccount);

                //Update lại tiền Checking
                for (CheckingAccount checkingAccount : checkingAccountList){
                    if (pAddress.equals(checkingAccount.getOwner())){
                        double amountCH = checkingAccount.getBalance();
                        checkingAccount.setBalance(amountCH + amount);
                        Model.getInstance().getDaoDriver().getCheckingAccountDao().updateCheckingAccount(checkingAccount);
                        break;
                    }
                }
                refreshDataLabel();
                break;
            }
        }
        // Nếu tền còn = 0 thì xóa luôn tài khoản
        List<SavingAccount> savingAccountList1 = Model.getInstance().getDaoDriver().getSavingAccountDao().getAllSavingAccounts();
        for (SavingAccount savingAccount : savingAccountList1){
            if(Sav_num.equals(savingAccount.getAccountNumber())){
                if(savingAccount.getBalance() < 1 ){
                    Model.getInstance().getDaoDriver().getSavingAccountDao().deleteSavingAccount(savingAccount.getOwner());
                    refreshDataLabel();
                    break;
                }
            }
        }
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
