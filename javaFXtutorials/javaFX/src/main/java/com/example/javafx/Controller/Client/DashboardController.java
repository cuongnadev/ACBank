package com.example.javafx.Controller.Client;

import com.example.javafx.Client.ClientHandler;
import com.example.javafx.Models.*;
import com.example.javafx.View.TransactionCellFactory;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import com.itextpdf.layout.properties.TextAlignment;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private String clientId;
    private ClientHandler clientHandler;
    private String pAddress = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshDataTransaction(clientId);
        List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
        transaction_listview.getItems().addAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory(clientId));
        send_money_btn.setOnAction(event -> {
            try {
                onSendMoney();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Model.getInstance().getViewFactory().setDashboardController(this);
    }


    public void setClientId(String clientId) { // Thêm setter này
        this.clientId = clientId;
        if(clientId != null) { // Cập nhật dữ liệu khi clientId được thiết lập
            setDataLabel(clientId);
            refreshDataTransaction(clientId);
        }
    }

    public void setDataLabel(String clientId){
        Platform.runLater(() -> {
            int Id = Integer.parseInt(clientId);
            List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
            List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
            List<Transaction> transactionList = Model.getInstance().getDaoDriver().getTransactionDao().getAllTransactions();

            for(Clients client : clientsList) {
                if(client.getId() == Id) {
                    pAddress = client.getPayeeAddress();
                    String LName = client.getLastName();
                    user_name.setText("Chào, "+ LName);
                    break;
                }
            }
            double income = 0;
            double expense = 0;
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);
            login_today.setText("Hôm nay, " + formattedDate);
            for (CheckingAccount checkingAccount : checkingAccountList){
                if (pAddress.equals(checkingAccount.getOwner())){
                    checking_acc_num.setText(checkingAccount.getAccountNumber());
                    checking_bal.setText(String.valueOf(checkingAccount.getBalance()));

                }
            }
            SavingAccount savingAccount = Model.getInstance().getDaoDriver().getSavingAccountDao().getSavingAccountsDataBalanceMax(pAddress);
            saving_acc_num.setText(savingAccount.getAccountNumber());
            saving_bal.setText(String.valueOf(savingAccount.getBalance()));

            for (Transaction transaction : transactionList){
                if (pAddress.equals(transaction.getSender())){
                    expense += transaction.getAmount();
                } else if (pAddress.equals(transaction.getReceiver())){
                    income += transaction.getAmount();
                }
            }
            income_lbl.setText(String.valueOf(income));
            expense_lbl.setText(String.valueOf(expense));
        });
    }

    //Send Money
    private void onSendMoney() throws IOException {
        clientHandler = Model.getInstance().getClientHandlers().get(clientId);
        System.out.println(clientId);
        System.out.println(clientHandler);
        if(clientHandler != null) {
            // Step 1: Lấy dữ liệu từ field
            String pAddress_receiver = payee_fld.getText().trim();
            double amount;
            try {
                amount = Double.parseDouble(amount_fld.getText().trim());
            } catch (NumberFormatException e) {
                showAlertErorr("Invalid amount. Please enter a valid number.");
                return;
            }
            String message = massage_fld.getText().trim();

            // Step 2: Xác thực dữ liệu
            if (pAddress_receiver.isEmpty() || amount <= 0) {
                showAlertErorr("Please enter valid Payee or Amount.");
                return;
            }

            Clients client = Model.getInstance().getDaoDriver().getClientsDao().getClientByPayeeAddress(pAddress_receiver);
            String receiverId = String.valueOf(client.getId());
            String messageForm = "transferMoney_" + clientId + "_" + receiverId + "_" + amount + "_" + message;

            System.out.println("[Client Log] --> " + messageForm);
            clientHandler.sendMessage(messageForm);
        }
    }



    private List<Transaction> getTransactionOfSQLiteLimit(int limit) {
        transaction_listview.getItems().clear();
//        String pAdress = Model.getInstance().getClients().getPayeeAddress();
        List<Transaction> transactionList = Model.getInstance().getDaoDriver().getTransactionDao().getAllTransactions();
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (pAddress.equals(transaction.getSender())
                || pAddress.equals(transaction.getReceiver())){
                Transaction newtTransaction = new Transaction(
                        transaction.getSender(),
                        transaction.getReceiver(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage());
                transactions.add(newtTransaction);
            }
        }
        // Sắp xếp danh sách theo ngày giảm dần
        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        // Giới hạn số lượng transactions
        return transactions.subList(0, Math.min(limit, transactions.size()));
    }

    public void refreshDataTransaction(String clientId){
        Platform.runLater(() -> {
            List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
            transaction_listview.getItems().setAll(transactions);
            transaction_listview.setCellFactory(listView -> new TransactionCellFactory(clientId));
        });
    }


    private void showAlertErorr(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
