package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.Controller.GetView.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;
    public ListView<Client> clients_listview1;
    public TextField pAddress_fld;
    public Button search_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Client> clients = getClientOfSQLite();
        clients_listview.getItems().addAll(clients);
        clients_listview.setCellFactory(listView -> new ClientCellFactory());
        Model.getInstance().getViewFactory().setClientsController(this);
        search_btn.setOnAction(event -> onSearch());
    }
    public void onSearch(){
        ResultSet resultSet= Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        String payeeAdress = pAddress_fld.getText().trim();
        Boolean check = false;
        clients_listview1.getItems().clear();
        try {
            while (resultSet.next()){
                if (payeeAdress.equals(resultSet.getString("PayeeAddress"))){
                    check = true;
                    Client client = new Client(
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getString("PayeeAddress"),
                            resultSet.getString("Password"),
                            new CheckingAccount(resultSet1.getString("Owner"),
                                    resultSet1.getString("AccountNumber"),
                                    resultSet1.getDouble("Balance"),
                                    resultSet1.getInt("TransactionLimit")),
                            new SavingAccount(resultSet2.getString("Owner"),
                                    resultSet2.getString("AccountNumber"),
                                    resultSet2.getDouble("Balance"),
                                    resultSet2.getInt("WithdrawalLimit")),
                            resultSet.getString("Date"));
                    clients_listview1.getItems().add(client);
                    clients_listview1.setCellFactory(listView -> new ClientCellFactory());
                }
            }
            if (check == false){
                showAlert("Error! Enter payee address no valid.");
                pAddress_fld.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void refreshClientsListView() {
        clients_listview.getItems().clear();
        List<Client> clients = getClientOfSQLite();
        clients_listview.getItems().setAll(clients);
        clients_listview1.getItems().clear();
        pAddress_fld.setText("");
    }
}
