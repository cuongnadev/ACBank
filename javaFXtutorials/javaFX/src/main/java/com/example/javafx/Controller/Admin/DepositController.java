package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.Controller.View.CheckingCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        String payeeAddress = pAddress_fld.getText().trim();
        int countSavAcc = 0;
        try {
            while (resultSet1.next()){
                if(resultSet1.getString("PayeeAddress").equals(payeeAddress)) {
                    first_name_lbl.setText(resultSet1.getString("FirstName"));
                    last_name_lbl.setText(resultSet1.getString("LastName"));
                    date_lbl.setText(resultSet1.getString("Date"));
                    break;
                }
            }
            while(resultSet2.next()) {
                if(resultSet2.getString("Owner").equals(payeeAddress)) {
                    countSavAcc++;
                }
            }
            num_of_saving_acc.setText(String.valueOf(countSavAcc));
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void onWithdrawal() {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
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
        try {
            while (resultSet.next()){
                if (resultSet.getString("Owner").equals(payeeAddress)){
                    double amountCH = Double.valueOf(resultSet.getString("Balance"));

                    if(amountCH < amount){
                        showAlertError("Insufficient funds. Please check your balance.");
                        return;
                    }
                    Model.getInstance().getDatabaseDriver().updateAccountBalance(payeeAddress, amountCH - amount);
                    showAlert("Withdrawal Successful!");
                    refreshData();
                    return;
                }
            }
            showAlertError("Error! Enter payee address no valid.");
            refreshData();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void onDeposit() {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
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
                Model.getInstance().getDatabaseDriver().insertSavingAccount(savingAccount);
                return;
            }
        }

        try {
            while (resultSet.next()){
                if (resultSet.getString("Owner").equals(payeeAddress)){
                    double amountCH = Double.valueOf(resultSet.getString("Balance"));
                    Model.getInstance().getDatabaseDriver().updateAccountBalance(payeeAddress, amountCH + amount);
                    showAlert("Deposit Successful!");
                    refreshData();
                    return;
                }
            }
            showAlertError("Error! Enter payee address no valid.");
            refreshData();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void onSearch(){
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        String payeeAdress = pAddress_fld.getText().trim();
        Boolean check = false;
        result_listview.getItems().clear();
        try {
            while (resultSet1.next()){
                if (payeeAdress.equals(resultSet1.getString("Owner"))){
                    check = true;
                    CheckingAccount checkingAccount = new CheckingAccount(
                                    resultSet1.getString("Owner"),
                                    resultSet1.getString("AccountNumber"),
                                    resultSet1.getDouble("Balance"),
                                    resultSet1.getInt("TransactionLimit"));

                    result_listview.getItems().add(checkingAccount);
                    result_listview.setCellFactory(listView -> new CheckingCellFactory());
                    setData();
                    break;
                }
            }
            if (check == false){
                showAlertError("Error! Enter payee address no valid.");
                refreshData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
