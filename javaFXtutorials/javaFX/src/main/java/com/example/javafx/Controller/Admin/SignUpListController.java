package com.example.javafx.Controller.Admin;

import com.example.javafx.Controller.View.ForgotPassCellFactory;
import com.example.javafx.Models.ForgotPass;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.SignUp;
import com.example.javafx.Controller.View.SignUpCellFactory;
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
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSignUpAccountData();

        List<SignUp> signUpList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                SignUp signUp = new SignUp(
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Password"),
                        resultSet.getString("PayeeAddress"),
                        resultSet.getDouble("CheckingAmount"),
                        resultSet.getDouble("SavingAmount"),
                        resultSet.getString("Date"),
                        resultSet.getString("CheckingNumber"),
                        resultSet.getString("SavingNumber"));

                signUpList.add(signUp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Sắp xếp danh sách theo ngày giảm dần
        signUpList.sort((t1, t2) -> t2.dateProperty().get().compareTo(t1.dateProperty().get()));
        return signUpList;
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
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getForgotPass();

        List<ForgotPass> List = new ArrayList<>();
        try {
            while (resultSet.next()) {
                ForgotPass pass = new ForgotPass(
                        resultSet.getString("PayeeAddress"),
                        resultSet.getString("Date"),
                        resultSet.getString("Email"));

                List.add(pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List;
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
