package com.example.javafx.Controller.Client;

import com.example.javafx.Models.Model;
import com.example.javafx.Models.Transaction;
import com.example.javafx.View.ClientMenuOptions;
import com.example.javafx.View.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button account_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }
    private void addListeners(){
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransactions());
        account_btn.setOnAction(event -> onAccounts());
        logout_btn.setOnAction(event -> onLogOut());
        profile_btn.setOnAction(event -> onProfile());
    }



    private void onDashboard (){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
        Model.getInstance().getViewFactory().getDashboardController().setDataLabel();
        Model.getInstance().getViewFactory().getDashboardController().refreshDataTransaction();
    }
    private void onTransactions (){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTION);
        Model.getInstance().getViewFactory().getTransactionsController().refreshData();
    }
    private void onAccounts (){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNT);
        Model.getInstance().getViewFactory().getAccountsController().refreshDataLabel();
    }
    private void onProfile() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }
    private void onLogOut(){
        Model.getInstance().getViewFactory().getLoginController().resetLoginClientForm();
        Model.getInstance().getViewFactory().showLoginWindow();
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
