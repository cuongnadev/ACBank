package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Client;
import com.example.javafx.Models.ForgotPass;
import com.example.javafx.Models.Model;
import com.example.javafx.View.ClientCellFactory;
import com.example.javafx.View.ForgotPassCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.ResourceBundle;

public class ForgotPassListController implements Initializable {
    public ListView forgotPass_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ForgotPass> forgotPasses = getForgotPassOfSQLite();
        forgotPass_listview.getItems().addAll(forgotPasses);
        forgotPass_listview.setCellFactory(listView -> new ForgotPassCellFactory());
        Model.getInstance().getViewFactory().setForgotPassListController(this);
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
}
