package com.example.javafx.Controller.Admin;

import com.example.javafx.View.ForgotPassCellFactory;
import com.example.javafx.Models.ForgotPass;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.SignUp;
import com.example.javafx.View.SignUpCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SignUpListController implements Initializable {


    public Text signup_lbl;
    public ListView signUp_listview;
    public Button create_btn;
    public Button forgot_btn;
    public ListView forgotPass_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<SignUp> receipts = getSignUpOfSQLite();
        signUp_listview.getItems().addAll(receipts);
        signUp_listview.setCellFactory(listView -> new SignUpCellFactory());
        Model.getInstance().getViewFactory().setSignUpListController(this);

        List<ForgotPass> forgotPasses = getForgotPassOfSQLite();
        forgotPass_listview.getItems().addAll(forgotPasses);
        forgotPass_listview.setCellFactory(listView -> new ForgotPassCellFactory());
        Model.getInstance().getViewFactory().setSignUpListController(this);

        create_btn.setOnAction(event -> onSignUp());
        forgot_btn.setOnAction(event -> onForgot());
    }

    public List<SignUp> getSignUpOfSQLite() {
        signUp_listview.getItems().clear();
        List<SignUp> signUpList = Model.getInstance().getDaoDriver().getSignUpDao().getAllsignUps();

        List<SignUp> signUps = new ArrayList<>();
        for (SignUp signUp : signUpList) {
            SignUp newSignUp = new SignUp(
                    signUp.getFirstName(),
                    signUp.getLastName(),
                    signUp.getPassword(),
                    signUp.getpAddress(),
                    signUp.getChAccBalance(),
                    signUp.getSvAccBalance(),
                    signUp.getDate(),
                    signUp.getCheckingNumber(),
                    signUp.getSavingNumber());

            signUps.add(newSignUp);
        }
        // Sắp xếp danh sách theo ngày giảm dần
        signUpList.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        return signUps;
    }

    private void onSignUp() {
        Stage stage = (Stage) signup_lbl.getScene().getWindow();
        try {
            // open signup window
            Model.getInstance().getViewFactory().showSignUpWindow();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onForgot(){
        Stage stage = (Stage) signup_lbl.getScene().getWindow();
        try {
            // open forgot window
            Model.getInstance().getViewFactory().showForgotPassWindow();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<ForgotPass> getForgotPassOfSQLite() {
        forgotPass_listview.getItems().clear();
        List<ForgotPass> forgotPassList = Model.getInstance().getDaoDriver().getForgotpassDao().getAllForgots();

        List<ForgotPass> forgotPasses = new ArrayList<>();
        for (ForgotPass forgotPass : forgotPassList) {
            ForgotPass pass = new ForgotPass(
                    forgotPass.getpAddress(),
                    forgotPass.getDate(),
                    forgotPass.getEmail());

            forgotPasses.add(pass);
        }
        return forgotPasses;
    }
    public void refreshClientsListView() {
        forgotPass_listview.getItems().clear();
        List<ForgotPass> forgotPasses = getForgotPassOfSQLite();
        forgotPass_listview.getItems().setAll(forgotPasses);

    }

    public void refreshSignUpListView() {
        signUp_listview.getItems().clear();
        List<SignUp> signUps = getSignUpOfSQLite();
        signUp_listview.getItems().setAll(signUps);
    }
}
