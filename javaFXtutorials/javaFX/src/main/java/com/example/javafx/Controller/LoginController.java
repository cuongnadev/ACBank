package com.example.javafx.Controller;

import com.example.javafx.Models.Model;
import com.example.javafx.View.AccountType;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Label payee_address_lbl;
    public TextField payee_address_fid;
    public PasswordField password_fid;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        payee_address_lbl.setText("Tên Đăng Nhập:");
        login_btn.setOnAction(event -> {
            try {
                onLogin();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Model.getInstance().getViewFactory().setLoginController(this);
    }


    private void onLogin() throws IOException {
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        //Login Admin
        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.ADMIN){
            //Evaluate Admin Login Credentials
            Model.getInstance().evaluateAdminCred(payee_address_fid.getText().trim() , password_fid.getText().trim());
            if (Model.getInstance().getAdminLoginSuccessFlag()){

                //khoi dong may chu
                Model.getInstance().startServer();

                //show admin
                Model.getInstance().getViewFactory().showAdminWindow();

                //Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            }else {
                payee_address_fid.setText("");
                password_fid.setText("");
                error_lbl.setText("No Such Login Credentials.");
            }
        }
    }

   //reset value login when login false or back login window
    public void resetLoginAdminForm(){
        payee_address_fid.setText("");
        password_fid.setText("");
        error_lbl.setText("");
        Model.getInstance().setAdminLoginSuccessFlag(false);
    }
}
