package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Client;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.Receipt;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class ReceiptCellController implements Initializable {
    public Label idBienLai_lbl;
    public Label sender_lbl;
    public Label receiver_lbl;
    public Label amount_lbl;
    public Label date_lbl;
    public Button delete_btn;
    private final Receipt receipt;
    public ReceiptCellController(Receipt receipt){
        this.receipt = receipt;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setReceiptData();
        delete_btn.setOnAction(event -> onDelete());
    }
    public void setReceiptData(){
        idBienLai_lbl.setText(receipt.IDReceiptProperty().get());
        sender_lbl.setText(receipt.senderProperty().get());
        receiver_lbl.setText(receipt.recerverProperty().get());
        date_lbl.setText(String.valueOf(receipt.dateProperty().get()));
        amount_lbl.setText(String.valueOf(receipt.amountProperty().get()));
    }

    public void onDelete(){
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getReceiptData();
        try {
            // Xóa receipt
            while (resultSet.next()){
                if (idBienLai_lbl.getText().equals(resultSet.getString("IDBienLai"))){
                    Model.getInstance().getDatabaseDriver().DropReceipt(idBienLai_lbl.getText());
                }
            }
            Model.getInstance().getViewFactory().getReceiptController().refreshReceiptListView();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
