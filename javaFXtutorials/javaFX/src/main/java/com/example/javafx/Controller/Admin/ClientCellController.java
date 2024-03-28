package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Client;
import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientCellController implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;
    public Button accept_btn;

    private final Client client;

    public ClientCellController (Client client){
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setClientData();
        delete_btn.setOnAction(event -> onDelete());
        accept_btn.setOnAction(event -> acceptClient());
    }



    public void setClientData(){
        fName_lbl.setText(client.firstNameProperty().get());
        lName_lbl.setText(client.lastNameProperty().get());
        pAddress_lbl.setText(client.pAddressProperty().get());
        date_lbl.setText(String.valueOf(client.dateCreatedProperty().get()));
        ch_acc_lbl.setText(client.checkingAccountProperty().get().accountNumberPropperty().get());
        sv_acc_lbl.setText(client.savingAccountProperty().get().accountNumberPropperty().get());
    }


    private void acceptClient() {
        Stage stage = (Stage) pAddress_lbl.getScene().getWindow();
        String password = null;
        // Tạo một hộp thoại đầu vào để nhập mật khẩu
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Password");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter your password:");

        // Hiển thị hộp thoại và chờ người dùng nhập mật khẩu
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            password = result.get().trim();
            Model.getInstance().evaluateClientCred(pAddress_lbl.getText().trim(), password);
            if (Model.getInstance().getClientLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().showClientWindow();

                //Close the login stage
                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("No Such Login Credentials");
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("User canceled the password input.");

        }
    }



    public void onDelete () {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
        ResultSet resultSet1 = Model.getInstance().getDatabaseDriver().getChekingAccountsData();
        ResultSet resultSet2 = Model.getInstance().getDatabaseDriver().getSavingAccountsData();
        ResultSet resultSet3 = Model.getInstance().getDatabaseDriver().getTransactionData();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Client " + pAddress_lbl.getText() + "?");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            try {
                // Xóa client
                while (resultSet.next()) {
                    if (pAddress_lbl.getText().equals(resultSet.getString("PayeeAddress"))) {
                        Model.getInstance().getDatabaseDriver().DropClient(pAddress_lbl.getText());
                    }
                }
                // Xóa CheckingAccounts
                while (resultSet1.next()) {
                    if (pAddress_lbl.getText().equals(resultSet1.getString("Owner"))) {
                        Model.getInstance().getDatabaseDriver().DropCheckingAccount(pAddress_lbl.getText());
                    }
                }
                // Xóa SavingsAccounts
                while (resultSet2.next()) {
                    if (pAddress_lbl.getText().equals(resultSet2.getString("Owner"))) {
                        Model.getInstance().getDatabaseDriver().DropSavingAccount(pAddress_lbl.getText());
                    }
                }
                // Xóa transaction
                while (resultSet3.next()) {
                    if (pAddress_lbl.getText().equals(resultSet3.getString("Sender")) || pAddress_lbl.getText().equals(resultSet3.getString("Receiver"))) {
                        Model.getInstance().getDatabaseDriver().DropTransaction(pAddress_lbl.getText());
                    }
                }
                Model.getInstance().getViewFactory().getClientsController().refreshClientsListView();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();
        } else {
            return;
        }
    }
}
