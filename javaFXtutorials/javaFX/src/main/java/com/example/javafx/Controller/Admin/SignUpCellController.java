package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
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
        List<SignUp> signUpList = Model.getInstance().getDaoDriver().getSignUpDao().getAllsignUps();
        // duyệt client
        for (SignUp signUp : signUpList){
            if (pAddress_lbl.getText().equals(signUp.getpAddress())){
                String adminName = Model.getInstance().getAdmin().getUserName();
                CheckingAccount checkingAccount = new CheckingAccount(signUp.getpAddress(), signUp.getCheckingNumber() ,signUp.getChAccBalance(), 100);
                SavingAccount savingAccount = new SavingAccount(signUp.getpAddress(), signUp.getSavingNumber() , signUp.getSvAccBalance(), 2000);
                Clients client = new Clients(signUp.getFirstName(), signUp.getLastName(),
                                            signUp.getpAddress(), signUp.getPassword(),
                                            signUp.getDate(), adminName);
                try {
                    Model.getInstance().getDaoDriver().getClientsDao().saveClient(client);
                    List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
                    for (Clients clients : clientsList){
                        if (clients.getPayeeAddress().equals(signUp.getpAddress())){
                            Model.getInstance().getDaoDriver().getCheckingAccountDao().saveCheckingAccount(checkingAccount);
                            Model.getInstance().getDaoDriver().getSavingAccountDao().saveSavingAccount(savingAccount);
                            Model.getInstance().getDaoDriver().getSignUpDao().deleteSignUp(pAddress_lbl.getText());
                            showAlertSuccessful("Client Create Successfully");
                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    showAlert("Error adding client. Please try again.");
                }
                Model.getInstance().getViewFactory().getSignUpListController().refreshSignUpListView();
            }
        }
        Model.getInstance().getViewFactory().getSignUpListController().refreshSignUpListView();

    }

    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAlertSuccessful(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setClientData(){
        fName_lbl.setText(signUp.getFirstName());
        lName_lbl.setText(signUp.getLastName());
        pAddress_lbl.setText(signUp.getpAddress());
        date_lbl.setText(String.valueOf(signUp.getDate()));
        ch_acc_lbl.setText(signUp.getCheckingNumber());
        sv_acc_lbl.setText(signUp.getSavingNumber());
    }

    public void onDelete(){
        List<SignUp> signUpList = Model.getInstance().getDaoDriver().getSignUpDao().getAllsignUps();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Client "+pAddress_lbl.getText()+"?");

        Optional<ButtonType> option = alert.showAndWait();
        if(option.get().equals(ButtonType.OK)){
            // Xóa client
            for (SignUp signUp : signUpList){
                if (pAddress_lbl.getText().equals(signUp.getpAddress())){
                    Model.getInstance().getDaoDriver().getSignUpDao().deleteSignUp(pAddress_lbl.getText());
                }
            }
            Model.getInstance().getViewFactory().getSignUpListController().refreshSignUpListView();
            alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();

        }else {
            return;
        }
    }
}
