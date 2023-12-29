package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.View.ClientCellFactory;
import com.example.javafx.View.ReceiptCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class ReceiptController implements Initializable {
    public ListView<Receipt> receipts_listview;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Receipt> receipts = getClientOfSQLite();
        receipts_listview.getItems().addAll(receipts);
        receipts_listview.setCellFactory(listView -> new ReceiptCellFactory());
        Model.getInstance().getViewFactory().setReceiptController(this);
    }

    public List<Receipt> getClientOfSQLite() {
        receipts_listview.getItems().clear();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getReceiptData();

        List<Receipt> receiptList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Receipt receipt = new Receipt(
                        resultSet.getString("IDBienLai"),
                        resultSet.getString("Sender"),
                        resultSet.getString("Receiver"),
                        resultSet.getDouble("Amount"),
                        resultSet.getString("Date"));

                receiptList.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Sắp xếp danh sách theo ngày giảm dần
        receiptList.sort((t1, t2) -> t2.dateProperty().get().compareTo(t1.dateProperty().get()));
        return receiptList;
    }
    public void refreshReceiptListView() {
        receipts_listview.getItems().clear();
        List<Receipt> receipts = getClientOfSQLite();
        receipts_listview.getItems().setAll(receipts);
    }
}
