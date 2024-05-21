package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.CheckingAccount;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
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
        owner_lbl.setText(checkingAccount.getOwner());
        ch_acc_num_lbl.setText(checkingAccount.getAccountNumber());
        limit_lbl.setText(String.valueOf(Integer.valueOf(checkingAccount.getTransactionLimit())));
        balance_lbl.setText(String.valueOf(Double.valueOf( checkingAccount.getBalance())));
    }
}
