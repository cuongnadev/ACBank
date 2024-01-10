package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.ForgotPass;
import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ForgotPassCellController implements Initializable {
    public Label pAddress_lbl;
    public Label date_lbl;
    public Button no_btn;
    public Button reset_btn;
    private final ForgotPass forgotPass;
    public ForgotPassCellController (ForgotPass forgotPass){
        this.forgotPass = forgotPass;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataLabel();
        reset_btn.setOnAction(event -> onReset());
        no_btn.setOnAction(event -> onNO());
    }


    private void setDataLabel() {
        pAddress_lbl.setText(forgotPass.pAddressProperty().get());
        date_lbl.setText(forgotPass.dateProperty().get());
    }
    private void onReset() {
        String newPass = Model.HashPassword("000000");
        Model.getInstance().getDatabaseDriver().updatepasswordClients(pAddress_lbl.getText() , newPass);
        Model.getInstance().getDatabaseDriver().DropForgotPass(pAddress_lbl.getText());
        Model.getInstance().getViewFactory().getForgotPassListController().refreshClientsListView();
        showAlertSuccessful("Reset Password of " + pAddress_lbl.getText() + " Successfull");
    }
    private void onNO() {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSignUpAccountData();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("You definitely don't want to reset password for "+pAddress_lbl.getText()+"?");

        Optional<ButtonType> option = alert.showAndWait();
        if(option.get().equals(ButtonType.OK)) {
            Model.getInstance().getDatabaseDriver().DropForgotPass(pAddress_lbl.getText());
            Model.getInstance().getViewFactory().getForgotPassListController().refreshClientsListView();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();
        }
    }
    public void showAlertSuccessful(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
