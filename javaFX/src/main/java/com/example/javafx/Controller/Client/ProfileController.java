package com.example.javafx.Controller.Client;

import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {


    public Label firstName_lbl;
    public Label lastName_lbl;
    public Label password_lbl;
    public Label pAddress_lbl;
    public TextField fName_fld;
    public TextField lName_fld;
    public TextField password_fld;
    public Button edit_btn;
    public Label date_lbl;
    public Label ch_acc_num_lbl;
    public Label bal_lbl;
    public Label num_of_sav_lbl;
    public Label income_lbl;
    public Label expense_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setdataLabel();
        edit_btn.setOnAction(event -> onEdit());
        Model.getInstance().getViewFactory().setProfileController(this);
    }
    public void setdataLabel() {
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        String password = Model.getInstance().getClient().passwordProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        ResultSet resultSet3 = Model.getInstance().getDatabaseDriver().getTransactionData();
        double income = 0;
        double expense = 0;
        try {
            while (resultSet.next()){
                if(pAddress.equals(resultSet.getString("PayeeAddress"))&&
                        password.equals(resultSet.getString("Password"))){
                    firstName_lbl.setText(resultSet.getString("FirstName"));
                    lastName_lbl.setText(resultSet.getString("LastName"));
                    password_lbl.setText(resultSet.getString("Password"));
                    pAddress_lbl.setText(resultSet.getString("PayeeAddress"));
                    date_lbl.setText(resultSet.getString("Date"));
                }
            }
            while (resultSet1.next()){
                if (pAddress.equals(resultSet1.getString("Owner"))){
                    ch_acc_num_lbl.setText(resultSet1.getString("AccountNumber"));
                }
            }
            int count = 0;
            while (resultSet2.next()){
                if (pAddress.equals(resultSet2.getString("Owner"))){
                    count = count +1;
                }
            }
            num_of_sav_lbl.setText(String.valueOf(count));

            while (resultSet3.next()){
                if (pAddress.equals(resultSet3.getString("Sender"))){
                    expense += resultSet3.getDouble("Amount");
                } else if (pAddress.equals(resultSet3.getString("Receiver"))){
                    income += resultSet3.getDouble("Amount");
                }
            }
            income_lbl.setText(String.valueOf(income));
            expense_lbl.setText(String.valueOf(expense));
            fName_fld.setText("");
            lName_fld.setText("");
            password_fld.setText("");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void onEdit() {
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();

            try {
                while (resultSet.next()){
                    if(pAddress.equals(resultSet.getString("PayeeAddress"))){
                        if (!(fName_fld.getText().trim().isEmpty())){
                            Model.getInstance().getDatabaseDriver().updateFNameClients(pAddress , fName_fld.getText());
                        }
                        if (!(lName_fld.getText().trim().isEmpty())){
                            Model.getInstance().getDatabaseDriver().updateLNameClients(pAddress , lName_fld.getText());
                        }
                        if (!(password_fld.getText().trim().isEmpty())){
                            String pass = Model.HashPassword(password_fld.getText());
                            Model.getInstance().getDatabaseDriver().updatepasswordClients(pAddress , pass);
                        }
                        setdataLabel();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }



    }

}
