package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpCellController implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;

    private final SignUp signUp;
    public Button duyet_btn;

    public SignUpCellController(SignUp signUp){
        this.signUp = signUp;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setClientData();
        delete_btn.setOnAction(event -> onDelete());
        duyet_btn.setOnAction(event -> onDuyet());
    }

    private void onDuyet() {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSignUpAccountData();
        try {
            // duyệt client
            while (resultSet.next()){
                if (pAddress_lbl.getText().equals(resultSet.getString("PayeeAddress"))){
                    CheckingAccount checkingAccount = new CheckingAccount(resultSet.getString("PayeeAddress"),resultSet.getString("CheckingNumber") ,resultSet.getDouble("CheckingAmount"), 10);
                    SavingAccount savingAccount = new SavingAccount(resultSet.getString("PayeeAddress"),resultSet.getString("SavingNumber") ,resultSet.getDouble("SavingAmount"), 2000);
                    Client client = new Client(resultSet.getString("FirstName"), resultSet.getString("LastName"),
                                                resultSet.getString("PayeeAddress"), resultSet.getString("Password"),
                                                checkingAccount, savingAccount ,resultSet.getString("Date"))  ;
                    try {
                        Model.getInstance().getDatabaseDriver().insertClient(client);
                        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getClientsData();
                        while (resultSet1.next()){
                            if (resultSet1.getString("PayeeAddress").equals(resultSet.getString("PayeeAddress"))){
                                Model.getInstance().getDatabaseDriver().insertCheckingAccount(checkingAccount);
                                Model.getInstance().getDatabaseDriver().insertSavingAccount(savingAccount);
                                showAlertSuccessful("Client Create Successfully");
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        showAlert("Error adding client. Please try again.");
                    }
                    onDelete();
                }
            }
            Model.getInstance().getViewFactory().getSignUpListController().refreshSignUpListView();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAlertSuccessful(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setClientData(){
        fName_lbl.setText(signUp.FirstNameProperty().get());
        lName_lbl.setText(signUp.lastNameProperty().get());
        pAddress_lbl.setText(signUp.pAddressProperty().get());
        date_lbl.setText(String.valueOf(signUp.dateProperty().get()));
        ch_acc_lbl.setText(signUp.checkingNumberProperty().get());
        sv_acc_lbl.setText(signUp.savingNumberProperty().get());
    }

    public void onDelete(){
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSignUpAccountData();
        try {
            // Xóa client
            while (resultSet.next()){
                if (pAddress_lbl.getText().equals(resultSet.getString("PayeeAddress"))){
                    Model.getInstance().getDatabaseDriver().DropSignUpAccount(pAddress_lbl.getText());
                }
            }
            Model.getInstance().getViewFactory().getSignUpListController().refreshSignUpListView();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
