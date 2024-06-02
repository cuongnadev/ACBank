package com.example.javafx.Controller.Client;

import com.example.javafx.Client.ClientSession;
import com.example.javafx.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public BorderPane client_parent;
    private final ClientSession clientSession;

    public ClientController(ClientSession clientSession) {
        this.clientSession = clientSession;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getDashboardController().setClientId(clientSession.getClientId());
        Model.getInstance().getViewFactory().getClientMenuController().setClientSession(clientSession);
        clientSession.selectedMenuItemProperty().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case TRANSACTION -> client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionsView(clientSession.getClientId()));
                case ACCOUNT -> client_parent.setCenter(Model.getInstance().getViewFactory().getAccountsView(clientSession.getClientId()));
                case PROFILE -> client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView(clientSession.getClientId()));
                default -> client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView(clientSession.getClientId()));
            }
        });
    }
}
