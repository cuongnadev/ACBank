package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Clients;
import com.example.javafx.Models.ForgotPass;
import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ForgotPassController implements Initializable {
    public TextField pAddress_fld;
    public Button send_btn;
    public Label label_lbl;
    public TextField email_fld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        send_btn.setOnAction(event -> onSend());
    }

    private void onSend() {
        List<ForgotPass> forgotPassList = Model.getInstance().getDaoDriver().getForgotpassDao().getAllForgots();
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        String pAddress = pAddress_fld.getText().trim();
        boolean check = false;
        // kiem tra du lieu
        for (ForgotPass forgotPass : forgotPassList) {
            if (pAddress.equals(forgotPass.getpAddress())) {
                showAlert("The request already exists!");
                pAddress_fld.setText("");
                return;
            } else {
                for (Clients client : clientsList) {
                    if(!client.getPayeeAddress().equals(pAddress)){
                        check = true;
                        break;
                    }
                }
                if(check) {
                    showAlert("Please enter a valid PayeeAddress!");
                    pAddress_fld.setText("");
                    return;
                } else {
                    Stage stage = (Stage) label_lbl.getScene().getWindow();
                    ForgotPass newForgotPass = new ForgotPass(pAddress_fld.getText(), LocalDate.now().toString() , email_fld.getText());
                    Model.getInstance().getDaoDriver().getForgotpassDao().saveForgotpass(newForgotPass);
                    showAlertSuccessful("The request has been successful, please wait for admin approval");
                    Model.getInstance().getViewFactory().getSignUpListController().refreshClientsListView();
                    //Close the SinUp stage
                    Model.getInstance().getViewFactory().closeStage(stage);
                    break;
                }
            }
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
}
