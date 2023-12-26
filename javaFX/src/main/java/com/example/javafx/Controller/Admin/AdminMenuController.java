package com.example.javafx.Controller.Admin;

import com.example.javafx.Controller.LoginController;
import com.example.javafx.Models.Model;
import com.example.javafx.View.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_btn;
    public Button clients_btn;
    public Button deposit_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }
    private void addListeners(){
        create_client_btn.setOnAction(event -> onCreateClient());
        clients_btn.setOnAction(event -> onClients());
        deposit_btn.setOnAction(event -> onDeposit());
        logout_btn.setOnAction(event -> onLogOut());
    }
    private void onCreateClient(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
        Model.getInstance().getViewFactory().getCreateClientController().refreshLabel();
    }
    private void onClients(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
        Model.getInstance().getViewFactory().getClientsController().refreshClientsListView();
    }
    private void onDeposit(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
    }
    private void onLogOut(){
        Model.getInstance().getViewFactory().showLoginWindow();
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().getLoginController().resetLoginAdminForm();

    }
}
