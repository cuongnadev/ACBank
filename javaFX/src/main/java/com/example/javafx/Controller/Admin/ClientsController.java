package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.View.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Client> clients = getClientOfSQLite();
        clients_listview.getItems().addAll(clients);
        clients_listview.setCellFactory(listView -> new ClientCellFactory());
        Model.getInstance().getViewFactory().setClientsController(this);
    }

    public List<Client> getClientOfSQLite() {
        clients_listview.getItems().clear();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();

        List<Client> clients = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("PayeeAddress"),
                        resultSet.getString("Password"),
                        newCheckingAccount(resultSet.getString("PayeeAddress")),
                        newSavingAccount(resultSet.getString("PayeeAddress")),
                        resultSet.getString("Date"));

                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
    public Account newCheckingAccount(String address) {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        Account newAccount = null;
        try {
            while (resultSet.next()) {
                if(address.equals(resultSet.getString("Owner"))){
                    newAccount = new CheckingAccount(resultSet.getString("Owner"),
                            resultSet.getString("AccountNumber"),
                            resultSet.getDouble("Balance"),
                            resultSet.getInt("TransactionLimit"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newAccount;
    }
    public Account newSavingAccount(String address) {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        Account newAccount = null;
        try {
            while (resultSet.next()) {
                if(address.equals(resultSet.getString("Owner"))){
                    newAccount = new SavingAccount(resultSet.getString("Owner"),
                            resultSet.getString("AccountNumber"),
                            resultSet.getDouble("Balance"),
                            resultSet.getInt("WithdrawalLimit"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newAccount;
    }
    public void refreshClientsListView() {
        clients_listview.getItems().clear();
        List<Client> clients = getClientOfSQLite();
        clients_listview.getItems().setAll(clients);
    }
}
