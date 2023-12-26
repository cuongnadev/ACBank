package com.example.javafx.Controller.Client;

import com.example.javafx.Controller.LoginController;
import com.example.javafx.Models.DatabaseDriver;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class TransactionCellController implements Initializable {
    public FontIcon in_icon;
    public FontIcon out_icon;
    public Label trans_date_lbl;
    public Label sender_lbl;
    public Label receiver_lbl;
    public Label amount_lbl;
    public FontIcon message_icon;

    private final Transaction transaction;

    public TransactionCellController(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataToLabels();
        message_icon.setOnMouseClicked(mouseEvent -> showMessage());
        in_out_icon();
    }

    private void setDataToLabels() {
        // Set the data from the transaction object to the corresponding labels
        trans_date_lbl.setText(transaction.dateProperty().get());
        sender_lbl.setText(transaction.senderProperty().get());
        receiver_lbl.setText(transaction.receiverProperty().get());
        amount_lbl.setText(String.valueOf(transaction.amountProperty().get()));
    }
    private void showMessage() {
        // Create an Alert dialog and show the message from the Transaction object
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transaction Message");
        alert.setHeaderText(null);
        alert.setContentText(transaction.messageProperty().get());
        alert.showAndWait();
    }
    private void in_out_icon(){
        LoginController loginController = Model.getInstance().getViewFactory().getLoginController();
        if (sender_lbl.getText().equals(loginController.payee_address_fid.getText())){
            in_icon.setIconColor(Color.RED);
            out_icon.setIconColor(Color.GREY);
        }
        if (receiver_lbl.getText().equals(loginController.payee_address_fid.getText())){
            out_icon.setIconColor(Color.GREEN);
            in_icon.setIconColor(Color.GREY);
        }
    }
}

