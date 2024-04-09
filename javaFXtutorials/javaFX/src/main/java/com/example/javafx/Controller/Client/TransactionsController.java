package com.example.javafx.Controller.Client;

import com.example.javafx.Models.Model;
import com.example.javafx.Models.Transaction;
import com.example.javafx.Controller.Other.TransactionCellFactory;
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
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getTransactionData();

        List<Transaction> transactions = new ArrayList<>();
        try {
            while (resultSet.next()) {
                if (pAddress.equals(resultSet.getString("Sender"))
                        || pAddress.equals(resultSet.getString("Receiver"))){
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
        return transactions;
    }
    public void refreshData() {
        List<Transaction> transactions = getTransactionOfSQLite();
        transaction_listview.getItems().setAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory());
    }
}
