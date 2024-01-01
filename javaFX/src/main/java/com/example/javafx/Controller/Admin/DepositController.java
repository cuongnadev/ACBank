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
import java.util.Map;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    public TextField pAddress_fld;
    public Button search_btn;
    public ListView<Client> result_listview;
    public TextField amount_fld;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(event -> onSearch());

    }

    public void onSearch(){
        ResultSet resultSet= Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        String payeeAdress = pAddress_fld.getText().trim();
        Boolean check = false;
        result_listview.getItems().clear();
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
                    result_listview.getItems().add(client);
                    result_listview.setCellFactory(listView -> new ClientCellFactory());
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


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertSucccessfully(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
