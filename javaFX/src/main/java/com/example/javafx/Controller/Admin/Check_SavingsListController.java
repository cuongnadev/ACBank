package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.*;
import com.example.javafx.Controller.GetView.CheckingCellFactory;
import com.example.javafx.Controller.GetView.SavingCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Check_SavingsListController implements Initializable {
    public TextField pAddress_fld;
    public Button search_btn;
    public ListView<CheckingAccount> result_listview;
    public ListView<SavingAccount> result_listview1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(event -> onSearch());
        refreshData();
        Model.getInstance().getViewFactory().setCheck_savingsListController(this);
    }

    public void onSearch(){
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        String payeeAdress = pAddress_fld.getText().trim();
        Boolean check = false;
        result_listview.getItems().clear();
        try {
            while (resultSet1.next()){
                if (payeeAdress.equals(resultSet1.getString("Owner"))){
                    check = true;
                    CheckingAccount checkingAccount = new CheckingAccount(
                                    resultSet1.getString("Owner"),
                                    resultSet1.getString("AccountNumber"),
                                    resultSet1.getDouble("Balance"),
                                    resultSet1.getInt("TransactionLimit"));

                    result_listview.getItems().add(checkingAccount);
                    result_listview.setCellFactory(listView -> new CheckingCellFactory());
                }
            }
            while (resultSet2.next()){
                if (payeeAdress.equals(resultSet2.getString("Owner"))){
                    check = true;
                    SavingAccount savingAccount = new SavingAccount(
                            resultSet2.getString("Owner"),
                            resultSet2.getString("AccountNumber"),
                            resultSet2.getDouble("Balance"),
                            resultSet2.getInt("WithdrawalLimit"));

                    result_listview1.getItems().add(savingAccount);
                    result_listview1.setCellFactory(listView -> new SavingCellFactory());
                }
            }
            if (check == false){
                showAlert("Error! Enter payee address no valid.");
                refreshData();
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

    public void refreshData(){
        pAddress_fld.setText("");
        result_listview.getItems().clear();
        result_listview1.getItems().clear();
    }
}
