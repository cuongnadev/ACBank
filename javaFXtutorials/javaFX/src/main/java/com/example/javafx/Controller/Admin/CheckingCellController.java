package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.CheckingAccount;
import com.example.javafx.Models.Client;
import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CheckingCellController implements Initializable {


    public Label owner_lbl;
    public Label ch_acc_num_lbl;
    public Label limit_lbl;
    public Label balance_lbl;
    public final CheckingAccount checkingAccount;
    public CheckingCellController(CheckingAccount checkingAccount){
        this.checkingAccount = checkingAccount;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataLabel();
    }

    private void setDataLabel() {
        owner_lbl.setText(checkingAccount.ownerProperty().get());
        ch_acc_num_lbl.setText(checkingAccount.accountNumberPropperty().get());
        limit_lbl.setText(String.valueOf(Integer.valueOf(checkingAccount.transactionLimitProperty().get())));
        balance_lbl.setText(String.valueOf(Double.valueOf( checkingAccount.balanceProperty().get())));
    }
}
