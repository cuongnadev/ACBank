package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.View.ReceiptCellFactory;
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
        List<Receipt> receiptList = Model.getInstance().getDaoDriver().getReceiptDao().getAllReceipts();
        String payeeAdress = IDReceipt_fld.getText().trim();
        String adminName = Model.getInstance().getAdmin().getUserName();
        boolean check = false;
        receipts_listview1.getItems().clear();
        for (Receipt receipt : receiptList){
            if (payeeAdress.equals(receipt.getIDReceipt())){
                check = true;
                Receipt newReceipt = new Receipt(receipt.getIDReceipt(),
                        receipt.getSender(),
                        receipt.getReceiver(),
                        receipt.getNumberSender(),
                        receipt.getNumberReceiver(),
                        receipt.getAmount(),
                        receipt.getDate(),
                        receipt.getMessage(),
                        receipt.getAdminName());
                receipts_listview1.getItems().add(newReceipt);
                receipts_listview1.setCellFactory(listView -> new ReceiptCellFactory());
            }
        }
        if (!check){
            showAlert("Error! Enter payee address no valid.");
            IDReceipt_fld.setText("");
        }
    }

    public List<Receipt> getReceiptOfSQLite() {
        receipts_listview.getItems().clear();
        List<Receipt> receipts = Model.getInstance().getDaoDriver().getReceiptDao().getAllReceipts();
        String adminName = Model.getInstance().getAdmin().getUserName();
        List<Receipt> receiptList = new ArrayList<>();
        for (Receipt receipt : receipts) {
            if(receipt.getAdminName().equals(adminName)) {
                Receipt newReceipt = new Receipt(receipt.getIDReceipt(),
                        receipt.getSender(),
                        receipt.getReceiver(),
                        receipt.getNumberSender(),
                        receipt.getNumberReceiver(),
                        receipt.getAmount(),
                        receipt.getDate(),
                        receipt.getMessage(),
                        receipt.getAdminName());

                receiptList.add(newReceipt);
            }
        }
        // Sắp xếp danh sách theo ngày giảm dần
        receiptList.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
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
