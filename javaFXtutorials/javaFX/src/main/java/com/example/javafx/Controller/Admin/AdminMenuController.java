package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Model;
import com.example.javafx.Controller.GetView.AdminMenuOptions;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    public Button clients_btn;
    public Button search_btn;
    public Button logout_btn;
    public Button receipts_btn;
    public Button signuplist_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }
    private void addListeners(){
        receipts_btn.setOnAction(event -> onReceipt());
        clients_btn.setOnAction(event -> onClients());
        search_btn.setOnAction(event -> onSearch_Check_Sav());
        logout_btn.setOnAction(event -> onLogOut());
        signuplist_btn.setOnAction(event -> onSignUpList());
    }

    private void onSignUpList() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.SIGNUPLIST);
        Model.getInstance().getViewFactory().getSignUpListController().refreshSignUpListView();
        Model.getInstance().getViewFactory().getSignUpListController().refreshClientsListView();
    }

    private void onReceipt(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.RECEIPT);
        Model.getInstance().getViewFactory().getReceiptController().refreshReceiptListView();
    }
    private void onClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
        Model.getInstance().getViewFactory().getClientsController().refreshClientsListView();
    }
    private void onSearch_Check_Sav(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.SAVINGSLIST);
        Model.getInstance().getViewFactory().getCheck_SavingsListController().refreshData();
    }
    private void onLogOut(){
        Model.getInstance().getViewFactory().showLoginWindow();
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().getLoginController().resetLoginAdminForm();

    }
}
