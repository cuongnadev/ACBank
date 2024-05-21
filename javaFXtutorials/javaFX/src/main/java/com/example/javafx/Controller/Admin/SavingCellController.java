package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.SavingAccount;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SavingCellController implements Initializable {


    public Label owner_lbl;
    public Label sv_acc_num_lbl;
    public Label limit_lbl;
    public Label balance_lbl;
    public final SavingAccount savingAccount;
    public SavingCellController(SavingAccount savingAccount){
        this.savingAccount = savingAccount;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataLabel();
    }

    private void setDataLabel() {
        owner_lbl.setText(savingAccount.getOwner());
        sv_acc_num_lbl.setText(savingAccount.getAccountNumber());
        limit_lbl.setText(String.valueOf(Double.valueOf(savingAccount.getWithdrawalLimit())));
        balance_lbl.setText(String.valueOf(Double.valueOf( savingAccount.getBalance())));
    }
}
