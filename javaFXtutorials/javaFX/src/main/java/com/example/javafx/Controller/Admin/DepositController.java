package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.View.CheckingCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    public TextField pAddress_fld;
    public Button search_btn;
    public ListView<CheckingAccount> result_listview;
    public Button deposit_btn;
    public Button withdrawal_btn;
    public TextField deposit_tfd;
    public TextField withdrawal_tfd;
    public Label num_of_saving_acc;
    public Label first_name_lbl;
    public Label last_name_lbl;
    public Label date_lbl;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(event -> onSearch());
        deposit_btn.setOnAction(event -> onDeposit());
        withdrawal_btn.setOnAction(event -> onWithdrawal());
        refreshData();
        Model.getInstance().getViewFactory().setDepositController(this);
    }

    public void setData() {
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        List<SavingAccount> savingAccountList = Model.getInstance().getDaoDriver().getSavingAccountDao().getAllSavingAccounts();
        String payeeAddress = pAddress_fld.getText().trim();
        int countSavAcc = 0;
        for (Clients client : clientsList){
            if(client.getPayeeAddress().equals(payeeAddress)) {
                first_name_lbl.setText(client.getFirstName());
                last_name_lbl.setText(client.getLastName());
                date_lbl.setText(client.getDateCreated());
                break;
            }
        }
        for (SavingAccount savingAccount : savingAccountList) {
            if(savingAccount.getOwner().equals(payeeAddress)) {
                countSavAcc++;
            }
        }
        num_of_saving_acc.setText(String.valueOf(countSavAcc));
    }

    private void onWithdrawal() {
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        String payeeAddress = pAddress_fld.getText().trim();
        Double amount = 0.0;
        try {
            amount = Double.parseDouble(withdrawal_tfd.getText().trim());
        } catch (NumberFormatException e) {
            showAlertError("Invalid amount. Please enter a valid number.");
            return;
        }
        // Kiểm tra tài khoản
        if (payeeAddress.isEmpty() ){
            showAlertError("Error! Please select the account you want to deposit.");
            return;
        }
        // kiểm tra số tiền
        if (amount <= 0){
            showAlertError("Please enter valid Amount.");
        }
        for (CheckingAccount checkingAccount : checkingAccountList){
            if (checkingAccount.getOwner().equals(payeeAddress)){
                double amountCH = Double.valueOf(checkingAccount.getBalance());

                if(amountCH < amount){
                    showAlertError("Insufficient funds. Please check your balance.");
                    return;
                }
                checkingAccount.setBalance(amountCH - amount);
                Model.getInstance().getDaoDriver().getCheckingAccountDao().updateCheckingAccount(checkingAccount);
                showAlert("Withdrawal Successful!");
                refreshData();
                return;
            }
        }
        showAlertError("Error! Enter payee address no valid.");
        refreshData();
    }

    private void onDeposit() {
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        String payeeAddress = pAddress_fld.getText().trim();
        Double amount = 0.0;
        try {
            amount = Double.parseDouble(deposit_tfd.getText().trim());
        } catch (NumberFormatException e) {
            showAlertError("Invalid amount. Please enter a valid number.");
            return;
        }
        // Kiểm tra tài khoản
        if (payeeAddress.isEmpty() ){
            showAlertError("Error! Please select the account you want to deposit.");
            return;
        }

        // kiểm tra số tiền
        if (amount <= 0){
            showAlertError("Please enter valid Amount.");
            return;
        }
        if (amount >= 2100000) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Would you like to create a savings account ?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                //Tạo account Saving
                String SavingNumber = "3021 " + RanDomNumber();
                SavingAccount savingAccount = new SavingAccount(payeeAddress, SavingNumber, amount, 2000);
                Model.getInstance().getDaoDriver().getSavingAccountDao().saveSavingAccount(savingAccount);
                return;
            }
        }

        for (CheckingAccount checkingAccount : checkingAccountList){
            if (checkingAccount.getOwner().equals(payeeAddress)){
                double amountCH = Double.valueOf(checkingAccount.getBalance());
                checkingAccount.setBalance(amountCH + amount);
                Model.getInstance().getDaoDriver().getCheckingAccountDao().updateCheckingAccount(checkingAccount);
                showAlert("Deposit Successful!");
                refreshData();
                return;
            }
        }
        showAlertError("Error! Enter payee address no valid.");
        refreshData();
    }

    public void onSearch(){
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        String payeeAdress = pAddress_fld.getText().trim();
        Boolean check = false;
        result_listview.getItems().clear();
        for (CheckingAccount checkingAccount : checkingAccountList){
            if (payeeAdress.equals(checkingAccount.getOwner())){
                check = true;
                CheckingAccount newCheckingAccount = new CheckingAccount(
                                checkingAccount.getOwner(),
                                checkingAccount.getAccountNumber(),
                                checkingAccount.getBalance(),
                                checkingAccount.getTransactionLimit());

                result_listview.getItems().add(newCheckingAccount);
                result_listview.setCellFactory(listView -> new CheckingCellFactory());
                setData();
                break;
            }
        }
        if (check == false){
            showAlertError("Error! Enter payee address no valid.");
            refreshData();
        }
    }



    private void showAlertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshData(){
        first_name_lbl.setText("...");
        last_name_lbl.setText("...");
        date_lbl.setText("0000-00-00");
        num_of_saving_acc.setText("0");
        pAddress_fld.setText("");
        deposit_tfd.setText("");
        withdrawal_tfd.setText("");
        result_listview.getItems().clear();
    }

    public int RanDomNumber(){
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}
