package com.example.javafx.Controller.Client;

import com.example.javafx.Models.Model;
import com.example.javafx.Models.Transaction;
import com.example.javafx.View.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;

public class TransactionsController implements Initializable {
    public ListView<Transaction> transaction_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshData();
        List<Transaction> transactions = getTransactionOfSQLite();
        transaction_listview.getItems().addAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory());
        Model.getInstance().getViewFactory().setTransactionsController(this);
    }

    public List<Transaction> getTransactionOfSQLite() {
        transaction_listview.getItems().clear();
        String pAddress = Model.getInstance().getClients().getPayeeAddress();
        List<Transaction> transactionList = Model.getInstance().getDaoDriver().getTransactionDao().getAllTransactions();
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (pAddress.equals(transaction.getSender())
                    || pAddress.equals(transaction.getReceiver())){
                Transaction newTransaction = new Transaction(
                        transaction.getSender(),
                        transaction.getReceiver(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage());
                transactions.add(newTransaction);
            }
        }
        // Sắp xếp danh sách theo ngày giảm dần
        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        return transactions;
    }
    public void refreshData() {
        List<Transaction> transactions = getTransactionOfSQLite();
        transaction_listview.getItems().setAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory());
    }
}
