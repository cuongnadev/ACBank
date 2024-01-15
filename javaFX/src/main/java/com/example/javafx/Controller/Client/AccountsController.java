package com.example.javafx.Controller.Client;

import com.example.javafx.Controller.LoginController;
import com.example.javafx.Models.*;
import com.example.javafx.View.CheckingCellFactory;
import com.example.javafx.View.ClientCellFactory;
import com.example.javafx.View.SavingCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;
    public TextField sav_acc_num_fld;
    public Button search_btn;
    public ListView<SavingAccount> savings_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshDataLabel();
        trans_to_sv_btn.setOnAction(event -> onTransToSV());
        trans_to_ch_btn.setOnAction(event -> onTransToCH());
        search_btn.setOnAction(event -> onSearch());
        List<SavingAccount> savingAccounts = getSavingOfSQLite();
        savings_listview.getItems().addAll(savingAccounts);
        savings_listview.setCellFactory(listView -> new SavingCellFactory());
        Model.getInstance().getViewFactory().setAccountsController(this);
    }

    private List<SavingAccount> getSavingOfSQLite() {
        savings_listview.getItems().clear();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        List<SavingAccount> savingAccounts = new ArrayList<>();
        try {
            while (resultSet.next()) {
                if(pAddress.equals(resultSet.getString("Owner"))){
                    SavingAccount savingAccount = new SavingAccount(
                            resultSet.getString("Owner"),
                            resultSet.getString("AccountNumber"),
                            resultSet.getDouble("Balance"),
                            resultSet.getInt("WithdrawalLimit"));

                    savingAccounts.add(savingAccount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        savingAccounts.sort((t1, t2) -> (String.valueOf(t2.balanceProperty().get()).compareTo(String.valueOf(t1.balanceProperty().get()))));
        return savingAccounts;
    }

    public void onSearch(){
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        String Sav_num = sav_acc_num_fld.getText();
        Boolean check = false;
        savings_listview.getItems().clear();
        try {
            
            while (resultSet1.next()){
                if (Sav_num.equals(resultSet1.getString("AccountNumber"))){
                    check = true;
                    SavingAccount savingAccount = new SavingAccount(
                            resultSet1.getString("Owner"),
                            resultSet1.getString("AccountNumber"),
                            resultSet1.getDouble("Balance"),
                            resultSet1.getInt("WithdrawalLimit"));

                    savings_listview.getItems().add(savingAccount);
                    savings_listview.setCellFactory(listView -> new SavingCellFactory());
                }
            }
            if (check == false){
                showAlert("Error! Enter payee address no valid.");
                sav_acc_num_fld.setText("");
                List<SavingAccount> savingAccounts = getSavingOfSQLite();
                savings_listview.getItems().addAll(savingAccounts);
                savings_listview.setCellFactory(listView -> new SavingCellFactory());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshDataLabel(){
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        String password = Model.getInstance().getClient().passwordProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        try {
            while (resultSet.next()) {
                if (pAddress.equals(resultSet.getString("PayeeAddress"))
                        && Model.HashPassword(password).equals(resultSet.getString("Password"))){
                    ch_acc_date.setText( resultSet.getString("Date"));
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
            sav_acc_num_fld.setText("");
            amount_to_ch.setText("");
            amount_to_sv.setText("");
            List<SavingAccount> savingAccounts = getSavingOfSQLite();
            savings_listview.getItems().setAll(savingAccounts);
            savings_listview.setCellFactory(listView -> new SavingCellFactory());

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

                    //Tạo account Saving
                    String SavingNumber = "3021 " + RanDomNumber();
                    SavingAccount savingAccount = new SavingAccount(pAddress , SavingNumber , amount , 2000);
                    Model.getInstance().getDatabaseDriver().insertSavingAccount(savingAccount);
                    refreshDataLabel();
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private void onTransToCH(){
        // Lấy dữ liệu
        String payeeAddress = Model.getInstance().getClient().pAddressProperty().get();
        String Sav_num = sav_acc_num_fld.getText();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();

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
                if(Sav_num.equals(resultSet.getString("AccountNumber"))){
                    double amountSV = Double.valueOf(resultSet.getString("Balance"));
                    if (amountSV < amount){
                        showAlert("Insufficient funds. Please check your balance.");
                        return;
                    }
                    //Update lại tiền Saving
                    Model.getInstance().getDatabaseDriver().updateSavingBalance(Sav_num,amountSV - amount);

                    //Update lại tiền Checking
                    while (resultSet1.next()){
                        if (payeeAddress.equals(resultSet1.getString("Owner"))){
                            double amountCH = Double.valueOf(resultSet1.getString("Balance"));
                            Model.getInstance().getDatabaseDriver().updateAccountBalance(payeeAddress,amountCH + amount);
                            break;
                        }
                    }
                    refreshDataLabel();
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public int RanDomNumber(){
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
