package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.View.ClientCellFactory;
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
    public ListView<Clients> clients_listview;
    public ListView<Clients> clients_listview1;
    public TextField pAddress_fld;
    public Button search_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Clients> clients = getClientOfSQLite();
        clients_listview.getItems().addAll(clients);
        clients_listview.setCellFactory(listView -> new ClientCellFactory());
        Model.getInstance().getViewFactory().setClientsController(this);
        search_btn.setOnAction(event -> onSearch());
    }
    public void onSearch(){
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        String payeeAdress = pAddress_fld.getText().trim();
        Boolean check = false;
        clients_listview1.getItems().clear();
        for (Clients client : clientsList){
            if (payeeAdress.equals(client.getPayeeAddress())){
                check = true;
                Clients newClient = new Clients(
                        client.getFirstName(),
                        client.getLastName(),
                        client.getPayeeAddress(),
                        client.getPassword(),
                        client.getDateCreated());
                clients_listview1.getItems().add(newClient);
                clients_listview1.setCellFactory(listView -> new ClientCellFactory());
            }
        }
        if (check == false){
            showAlert("Error! Enter payee address no valid.");
            pAddress_fld.setText("");
        }
    }




    public List<Clients> getClientOfSQLite() {
        clients_listview.getItems().clear();
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();

        List<Clients> clients = new ArrayList<>();
        for (Clients client : clientsList) {
            Clients newClient = new Clients(
                    client.getFirstName(),
                    client.getLastName(),
                    client.getPayeeAddress(),
                    client.getPassword(),
                    client.getDateCreated());

            clients.add(newClient);
        }
        return clients;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void refreshClientsListView() {
        clients_listview.getItems().clear();
        List<Clients> clients = getClientOfSQLite();
        clients_listview.getItems().setAll(clients);
        clients_listview1.getItems().clear();
        pAddress_fld.setText("");
    }
}
