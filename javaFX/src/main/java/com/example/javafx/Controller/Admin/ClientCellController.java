package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Client;
import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;

    private final Client client;

    public ClientCellController (Client client){
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setClientData();
        delete_btn.setOnAction(event -> onDelete());
    }
    public void setClientData(){
        fName_lbl.setText(client.firstNameProperty().get());
        lName_lbl.setText(client.lastNameProperty().get());
        pAddress_lbl.setText(client.pAddressProperty().get());
        date_lbl.setText(String.valueOf(client.dateCreatedProperty().get()));
        ch_acc_lbl.setText(client.checkingAccountProperty().get().accountNumberPropperty().get());
        sv_acc_lbl.setText(client.savingAccountProperty().get().accountNumberPropperty().get());
    }

    public void onDelete(){
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        ResultSet resultSet3 = Model.getInstance().getDatabaseDriver().getTransactionData();

        try {
            // Xóa client
            while (resultSet.next()){
                if (pAddress_lbl.getText().equals(resultSet.getString("PayeeAddress"))){
                    Model.getInstance().getDatabaseDriver().DropClient(pAddress_lbl.getText());
                }
            }
            // Xóa CheckingAccounts
            while (resultSet1.next()){
                if (pAddress_lbl.getText().equals(resultSet1.getString("Owner"))){
                    Model.getInstance().getDatabaseDriver().DropCheckingAccount(pAddress_lbl.getText());
                }
            }
            // Xóa SavingsAccounts
            while (resultSet2.next()){
                if (pAddress_lbl.getText().equals(resultSet2.getString("Owner"))){
                    Model.getInstance().getDatabaseDriver().DropSavingAccount(pAddress_lbl.getText());
                }
            }
            // Xóa transaction
            while (resultSet3.next()){
                if (pAddress_lbl.getText().equals(resultSet3.getString("Sender")) || pAddress_lbl.getText().equals(resultSet3.getString("Receiver"))){
                    Model.getInstance().getDatabaseDriver().DropTransaction(pAddress_lbl.getText());
                }
            }
            Model.getInstance().getViewFactory().getClientsController().refreshClientsListView();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
