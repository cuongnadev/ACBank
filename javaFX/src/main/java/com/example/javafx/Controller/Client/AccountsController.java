package com.example.javafx.Controller.Client;

import com.example.javafx.Controller.LoginController;
import com.example.javafx.Models.DatabaseDriver;
import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sefreshDataLabel();
        trans_to_sv_btn.setOnAction(event -> onTransToSV());
        trans_to_ch_btn.setOnAction(event -> onTransToCH());
        Model.getInstance().getViewFactory().setAccountsController(this);
    }
    public void sefreshDataLabel(){
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        String password = Model.getInstance().getClient().passwordProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        try {
            while (resultSet.next()) {
                if (pAddress.equals(resultSet.getString("PayeeAddress"))
                        && password.equals(resultSet.getString("Password"))){
                    ch_acc_date.setText( resultSet.getString("Date"));
                    sv_acc_date.setText( resultSet.getString("Date"));
                    break;
                }

            }
            while (resultSet1.next()){
                if (pAddress.equals(resultSet1.getString("Owner"))){
                    ch_acc_num.setText(resultSet1.getString("AccountNumber"));
                    ch_acc_bal.setText(resultSet1.getString("Balance"));
                    transaction_limit.setText(resultSet1.getString("TransactionLimit"));
                    break;
                }
            }
            while (resultSet2.next()){
                if (pAddress.equals(resultSet2.getString("Owner"))){
                    sv_acc_num.setText(resultSet2.getString("AccountNumber"));
                    sv_acc_bal.setText(resultSet2.getString("Balance"));
                    withdrawal_limit.setText(resultSet2.getString("WithdrawalLimit"));
                    break;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void onTransToSV(){
        // Lấy dữ liệu
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        String payeeAddress = pAddress.trim();
        double amount;
        try {
            amount = Double.parseDouble(amount_to_sv.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Invalid amount. Please enter a valid number.");
            return;
        }
        // Kiểm tra dữ liệu
        if (amount <= 0){
            showAlert("Please enter valid Amount.");
        }
        try {
            //Truy xuất balance từ tài khoản checking
            while (resultSet1.next()){
                if(payeeAddress.equals(resultSet1.getString("Owner"))){
                    double amountCH = Double.valueOf(resultSet1.getString("Balance"));
                    //update lại sau khi gửi tiền tiết kiệm
                    if (amountCH < amount){
                        showAlert("Insufficient funds. Please check your balance.");
                        return;
                    }
                    //Update lại tiền Checking
                    Model.getInstance().getDatabaseDriver().updateAccountBalance(payeeAddress,amountCH - amount);

                    //Update lại tiền Saving
                    while (resultSet.next()){
                        if (payeeAddress.equals(resultSet.getString("Owner"))){
                            double amountSV = Double.valueOf(resultSet.getString("Balance"));
                            Model.getInstance().getDatabaseDriver().updateSavingBalance(payeeAddress,amountSV + amount);
                            break;
                        }
                    }
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private void onTransToCH(){
        // Lấy dữ liệu
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        String payeeAddress = pAddress.trim();
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
        try {
            //Truy xuất balance từ tài khoản saving
            while (resultSet.next()){
                if(payeeAddress.equals(resultSet.getString("Owner"))){
                    double amountSV = Double.valueOf(resultSet.getString("Balance"));
                    if (amountSV < amount){
                        showAlert("Insufficient funds. Please check your balance.");
                        return;
                    }
                    //Update lại tiền Saving
                    Model.getInstance().getDatabaseDriver().updateSavingBalance(payeeAddress,amountSV - amount);

                    //Update lại tiền Checking
                    while (resultSet1.next()){
                        if (payeeAddress.equals(resultSet1.getString("Owner"))){
                            double amountCH = Double.valueOf(resultSet1.getString("Balance"));
                            Model.getInstance().getDatabaseDriver().updateAccountBalance(payeeAddress,amountCH + amount);
                            break;
                        }
                    }
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
