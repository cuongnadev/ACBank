package com.example.javafx.Controller.Client;

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
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getForgotPass();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getClientsData();
        String pAddress = pAddress_fld.getText().trim();
        boolean check = false;
        try {
            if(!resultSet.isBeforeFirst()){
                if(!resultSet1.isBeforeFirst()){
                    showAlert("Please enter a valid PayeeAddress!");
                    pAddress_fld.setText("");
                    return;
                }
                while (resultSet1.next()) {
                    if (pAddress.equals(resultSet1.getString("PayeeAddress"))) {
                        check = true;
                        break;
                    }
                }
                if (check == false) {
                    showAlert("Please enter a valid PayeeAddress!");
                    pAddress_fld.setText("");
                } else {
                    Stage stage = (Stage) label_lbl.getScene().getWindow();
                    ForgotPass forgotPass = new ForgotPass(pAddress_fld.getText(), LocalDate.now().toString(),email_fld.getText() );
                    Model.getInstance().getDatabaseDriver().insertForgotPass(forgotPass);
                    showAlertSuccessful("The request has been successful, please wait for admin approval");
                    //Close the SinUp stage
                    Model.getInstance().getViewFactory().closeStage(stage);
                    //Open the login window
                    Model.getInstance().getViewFactory().showLoginWindow();
                }
            }
            // kiem tra du lieu
            while (resultSet.next()) {
                if (pAddress.equals(resultSet.getString("PayeeAddress"))) {
                    showAlert("The request already exists!");
                    pAddress_fld.setText("");
                    return;
                } else {
                    if(!resultSet1.isBeforeFirst()){
                        showAlert("Please enter a valid PayeeAddress!");
                        pAddress_fld.setText("");
                        return;
                    }
                    while (resultSet1.next()) {
                        if (pAddress.equals(resultSet1.getString("PayeeAddress"))) {
                            check = true;
                            break;
                        }
                    }
                    if (check == false) {
                        showAlert("Please enter a valid PayeeAddress!");
                        pAddress_fld.setText("");
                    } else {
                        Stage stage = (Stage) label_lbl.getScene().getWindow();
                        ForgotPass forgotPass = new ForgotPass(pAddress_fld.getText(), LocalDate.now().toString() , email_fld.getText());
                        Model.getInstance().getDatabaseDriver().insertForgotPass(forgotPass);
                        showAlertSuccessful("The request has been successful, please wait for admin approval");
                        //Close the SinUp stage
                        Model.getInstance().getViewFactory().closeStage(stage);
                        //Open the login window
                        Model.getInstance().getViewFactory().showLoginWindow();
                        break;
                    }
                }
            }
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
}
