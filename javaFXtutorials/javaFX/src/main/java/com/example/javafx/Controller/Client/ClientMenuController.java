package com.example.javafx.Controller.Client;

import com.example.javafx.Client.ClientSession;
import com.example.javafx.Models.Model;
import com.example.javafx.View.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button account_btn;
    public Button profile_btn;
    public Button logout_btn;
    private ClientSession clientSession;

    public void setClientSession(ClientSession clientSession) {
        this.clientSession = clientSession;
        addListeners();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        Model.getInstance().getViewFactory().setClientMenuController(this);
    }
    private void addListeners(){
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransactions());
        account_btn.setOnAction(event -> onAccounts());
        logout_btn.setOnAction(event -> onLogOut());
        profile_btn.setOnAction(event -> onProfile());
    }



    private void onDashboard (){
        clientSession.setSelectedMenuItem(ClientMenuOptions.DASHBOARD);
    }
    private void onTransactions (){
        clientSession.setSelectedMenuItem(ClientMenuOptions.TRANSACTION);
    }
    private void onAccounts (){
        clientSession.setSelectedMenuItem(ClientMenuOptions.ACCOUNT);
    }
    private void onProfile() {
        clientSession.setSelectedMenuItem(ClientMenuOptions.PROFILE);
    }
    private void onLogOut(){
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().setClientLoginSuccessFlag(false);
    }
}
