package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.Controller.View.ReceiptCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReceiptController implements Initializable {
    public ListView<Receipt> receipts_listview;
    public ListView<Receipt> receipts_listview1;
    public TextField IDReceipt_fld;
    public Button search_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Receipt> receipts = getReceiptOfSQLite();
        receipts_listview.getItems().addAll(receipts);
        receipts_listview.setCellFactory(listView -> new ReceiptCellFactory());
        Model.getInstance().getViewFactory().setReceiptController(this);
        search_btn.setOnAction(event -> onSearch());
    }

    public void onSearch(){
        ResultSet resultSet= Model.getInstance().getDatabaseDriver().getReceiptData();
        String payeeAdress = IDReceipt_fld.getText().trim();
        Boolean check = false;
        receipts_listview1.getItems().clear();
        try {
            while (resultSet.next()){
                if (payeeAdress.equals(resultSet.getString("IDBienLai"))){
                    check = true;
                    Receipt receipt = new Receipt(resultSet.getString("IDBienLai"),
                            resultSet.getString("Sender"),
                            resultSet.getString("Receiver"),
                            resultSet.getDouble("Amount"),
                            resultSet.getString("Date"));
                    receipts_listview1.getItems().add(receipt);
                    receipts_listview1.setCellFactory(listView -> new ReceiptCellFactory());
                }
            }
            if (check == false){
                showAlert("Error! Enter payee address no valid.");
                IDReceipt_fld.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Receipt> getReceiptOfSQLite() {
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void refreshReceiptListView() {
        receipts_listview.getItems().clear();
        List<Receipt> receipts = getReceiptOfSQLite();
        receipts_listview.getItems().setAll(receipts);
        receipts_listview1.getItems().clear();
        IDReceipt_fld.setText("");
    }
}
