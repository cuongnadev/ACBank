package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Model;
import com.example.javafx.Models.Receipt;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
        idBienLai_lbl.setText(receipt.getIDReceipt());
        sender_lbl.setText(receipt.getSender());
        receiver_lbl.setText(receipt.getReceiver());
        date_lbl.setText(String.valueOf(receipt.getDate()));
        amount_lbl.setText(String.valueOf(receipt.getAmount()));
    }

    public void onDelete(){
        List<Receipt> receiptList = Model.getInstance().getDaoDriver().getReceiptDao().getAllReceipts();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("You definitely don't want to reset password for "+idBienLai_lbl.getText()+"?");

        Optional<ButtonType> option = alert.showAndWait();
        if(option.get().equals(ButtonType.OK)) {
            // Xóa receipt
            for (Receipt receipt : receiptList){
                if (idBienLai_lbl.getText().equals(receipt.getIDReceipt())){
                    Model.getInstance().getDaoDriver().getReceiptDao().deleteReceipt(idBienLai_lbl.getText());
                }
            }
            Model.getInstance().getViewFactory().getReceiptController().refreshReceiptListView();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();
        }

    }
}
