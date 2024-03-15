package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case RECEIPT -> admin_parent.setCenter(Model.getInstance().getViewFactory().getReceiptView());
                case SAVINGSLIST -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCheck_SavingsListView());
                case SIGNUPLIST -> admin_parent.setCenter(Model.getInstance().getViewFactory().getSignUpListView());
                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getClientsView());
            }
        });
    }
}
