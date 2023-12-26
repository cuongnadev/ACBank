package com.example.javafx.Controller.Client;

import com.example.javafx.Controller.LoginController;
import com.example.javafx.Models.DatabaseDriver;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.Transaction;
import com.example.javafx.View.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_today;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label saving_bal;
    public Label saving_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView<Transaction> transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea massage_fld;
    public Button send_money_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataLabel();
        refreshDataTransaction();
        List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
        transaction_listview.getItems().addAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory());
        send_money_btn.setOnAction(event -> onSendMoney());
        Model.getInstance().getViewFactory().setDashboardController(this);
    }

    public void setDataLabel(){
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        String password = Model.getInstance().getClient().passwordProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        ResultSet resultSet3 = Model.getInstance().getDatabaseDriver().getTransactionData();
        double income = 0;
        double expense = 0;
        try {
            while (resultSet.next()) {
                if (pAddress.equals(resultSet.getString("PayeeAddress"))
                        && password.equals(resultSet.getString("Password"))){
                    String LName = resultSet.getString("LastName");
                    System.out.println(LName);
                    user_name.setText("Hi, "+ LName);
                }
            }
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);
            login_today.setText("Today, " + formattedDate);
            while (resultSet1.next()){
                if (pAddress.equals(resultSet1.getString("Owner"))){
                    checking_acc_num.setText(resultSet1.getString("AccountNumber"));
                    checking_bal.setText(resultSet1.getString("Balance"));

                }
            }
            while (resultSet2.next()){
                if (pAddress.equals(resultSet2.getString("Owner"))){
                    saving_acc_num.setText(resultSet2.getString("AccountNumber"));
                    saving_bal.setText(resultSet2.getString("Balance"));

                }
            }
            while (resultSet3.next()){
                if (pAddress.equals(resultSet3.getString("Sender"))){
                    expense += resultSet3.getDouble("Amount");
                } else if (pAddress.equals(resultSet3.getString("Receiver"))){
                    income += resultSet3.getDouble("Amount");
                }
            }
            income_lbl.setText(String.valueOf(income));
            expense_lbl.setText(String.valueOf(expense));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Send Money
    private void onSendMoney() {
        // Step 1: Lấy dữ liệu từ field
        String payeeAddress = payee_fld.getText().trim();
        double amount;
        try {
            amount = Double.parseDouble(amount_fld.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Invalid amount. Please enter a valid number.");
            return;
        }
        String message = massage_fld.getText().trim();

        // Step 2: Xác thực dữ liệu
        if (payeeAddress.isEmpty() || amount <= 0) {
            showAlert("Please enter valid Payee or Amount.");
            return;
        }

        // Step 3:Cập nhật số dư tài khoản và thêm transaction mới
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();

        // Truy xuất thông tin tài khoản của người gửi
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        try {
            while (resultSet.next()) {
                if (pAddress.equals(resultSet.getString("Owner"))) {
                    double senderBalance = resultSet.getDouble("Balance");
                    if (senderBalance < amount) {
                        showAlert("Insufficient funds. Please check your balance.");
                        return;
                    }
                    // Truy xuất thông tin tài khoản của người nhận thanh toán
                    ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
                    try {
                        while (resultSet1.next()) {
                            if (payeeAddress.equals(resultSet1.getString("Owner"))) {
                                // Cập nhật số dư của người gửi
                                Model.getInstance().getDatabaseDriver().updateAccountBalance(pAddress, senderBalance - amount);

                                // Cập nhật số dư người nhận
                                double payeeBalance = resultSet1.getDouble("Balance");
                                Model.getInstance().getDatabaseDriver().updateAccountBalance(payeeAddress, payeeBalance + amount);

                                //Thêm giao dịch mới
                                Transaction newTransaction = new Transaction(
                                        pAddress,
                                        payeeAddress,
                                        amount,
                                        LocalDate.now().toString(),
                                        message);
                                Model.getInstance().getDatabaseDriver().insertTransaction(newTransaction);

                                // Làm mới transaction listview
                                List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
                                transaction_listview.getItems().setAll(transactions);
                            }
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("An error occurred while processing the transaction.");
        }
    }


    private List<Transaction> getTransactionOfSQLiteLimit(int limit) {
        transaction_listview.getItems().clear();
        String pAdress = Model.getInstance().getClient().pAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getTransactionData();
        List<Transaction> transactions = new ArrayList<>();
        try {
            while (resultSet.next()) {
                if (pAdress.equals(resultSet.getString("Sender"))
                    || pAdress.equals(resultSet.getString("Receiver"))){
                    Transaction transaction = new Transaction(
                            resultSet.getString("Sender"),
                            resultSet.getString("Receiver"),
                            resultSet.getDouble("Amount"),
                            resultSet.getString("Date"),
                            resultSet.getString("Message"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Sắp xếp danh sách theo ngày giảm dần
        transactions.sort((t1, t2) -> t2.dateProperty().get().compareTo(t1.dateProperty().get()));

        // Giới hạn số lượng transactions
        return transactions.subList(0, Math.min(limit, transactions.size()));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void refreshDataTransaction(){
        List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
        transaction_listview.getItems().setAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory());
    }
}
