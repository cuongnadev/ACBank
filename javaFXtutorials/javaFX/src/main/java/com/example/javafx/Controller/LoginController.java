package com.example.javafx.Controller;

import com.example.javafx.Models.Model;
import com.example.javafx.Controller.GetView.AccountType;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public ChoiceBox<AccountType> acc_selector;
    public Label payee_address_lbl;
    public TextField payee_address_fid;
    public PasswordField password_fid;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT , AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue()));
        acc_selector.valueProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().getViewFactory().setLoginAccountType(newValue);
            if (newValue == AccountType.ADMIN) {
                //set the label text is Username:
                payee_address_lbl.setText("UserName:");
            } else {
                // Set the label text to a default value for CLIENT
                payee_address_lbl.setText("Payee Address:");
            }
        });
        login_btn.setOnAction(event -> onLogin());
        Model.getInstance().getViewFactory().setLoginController(this);
    }


    private void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();

//        //Login Client
//        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT){
//
//            //Evaluate Client Login Credentials
//            Model.getInstance().evaluateClientCred(payee_address_fid.getText().trim() , password_fid.getText().trim());
//            if (Model.getInstance().getClientLoginSuccessFlag()){
//                Model.getInstance().getViewFactory().showClientWindow();
//
//                //Close the login stage
//                Model.getInstance().getViewFactory().closeStage(stage);
//            }else{
//                payee_address_fid.setText("");
//                password_fid.setText("");
//                error_lbl.setText("No Such Login Credentials.");
//            }
//        } else {
            //Login Admin
            if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.ADMIN){
                //Evaluate Admin Login Credentials
                Model.getInstance().evaluateAdminCred(payee_address_fid.getText().trim() , password_fid.getText().trim());
                if (Model.getInstance().getAdminLoginSuccessFlag()){
                    Model.getInstance().getViewFactory().showAdminWindow();

                    //Close the login stage
                    Model.getInstance().getViewFactory().closeStage(stage);
                }else{
                    payee_address_fid.setText("");
                    password_fid.setText("");
                    error_lbl.setText("No Such Login Credentials.");
                }
            }
//        }
    }
   //reset value login when login false or back login window
    public void resetLoginClientForm(){
        this.payee_address_fid.setText("");
        this.password_fid.setText("");
        error_lbl.setText("");
        Model.getInstance().setClientLoginSuccessFlag(false);
    }
    public void resetLoginAdminForm(){
        payee_address_fid.setText("");
        password_fid.setText("");
        error_lbl.setText("");
        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
