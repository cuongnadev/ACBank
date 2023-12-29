package com.example.javafx.Controller.Client;

import com.example.javafx.Models.CheckingAccount;
import com.example.javafx.Models.Client;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.SavingAccount;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;

public class SignInController implements Initializable {
    public TextField fName__fld;
    public TextField lName_fld;
    public TextField password_fld;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fld;
    public CheckBox sv_acc_box;
    public TextField sv_amount_fld;
    public Button create_client_btn;
    public Label error_lbl;

    public TextField lNameEdit_fld;
    public TextField passwordEdit_fld;
    public TextField pAddressEdit_fld;
    public Button edit_customer_btn;
    public TextField fNameEdit_fld;
    public Label error_lbl1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> onCreate());
        pAddress_box.setOnAction(event -> onpAddressBox());
        edit_customer_btn.setOnAction(event -> onEdit());
        Model.getInstance().getViewFactory().setSignInController(this);
    }

    private void onEdit() {
        // Lấy dữ liệu đầu vào
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
        String firstName = fNameEdit_fld.getText().trim();
        String lastName = lNameEdit_fld.getText().trim();
        String password = passwordEdit_fld.getText().trim();
        boolean check = false;
        try {
            while (resultSet.next()){
                if(pAddressEdit_fld.getText().equals(resultSet.getString("PayeeAddress"))){
                    check = true;
                    // Kiểm tra dữ liệu và update
                    if(!(firstName.isEmpty())){
                        Model.getInstance().getDatabaseDriver().updateFNameClients(pAddressEdit_fld.getText() , firstName);
                        showAlert("Update First Name Successfully! /n" +
                                  "New First Name: " + firstName);
                    }
                    if(!(lastName.isEmpty())){
                        Model.getInstance().getDatabaseDriver().updateLNameClients(pAddressEdit_fld.getText() , lastName);
                        showAlert("Update Last Name Successfully! /n" +
                                "New Last Name: " + lastName);
                    }
                    if(!(password.isEmpty())){
                        Model.getInstance().getDatabaseDriver().updatepasswordClients(pAddressEdit_fld.getText() , password);
                        showAlert("Update Password Successfully! /n" +
                                "New Password: " + password);
                    }
                    if (firstName.isEmpty() && lastName.isEmpty() && password.isEmpty()){
                        showAlert("Please, Enter valid data!");
                    }
                }
                if(check == false){
                    showAlert("No account found for Payee Address: " + pAddressEdit_fld);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private void onpAddressBox() {
        if (pAddress_box.isSelected()) {
            // Được chọn, tạo địa chỉ ngẫu nhiên
            String firstName = fName__fld.getText().trim();
            String lastName = lName_fld.getText().trim();
            String randomAddress = RanDomAddress(firstName, lastName);
            pAddress_lbl.setText(randomAddress);
        } else {
            // Không được chọn, sử dụng địa chỉ mặc định
            pAddress_lbl.setText("@"+ lName_fld.getText() + fName__fld.getText());
        }
    }

    private void onCreate() {
        //Lấy dữ liệu đầu vào từ field
        String firstName = fName__fld.getText().trim();
        String lastName = lName_fld.getText().trim();
        String password = password_fld.getText().trim();
        String pAddress = pAddress_lbl.getText().trim();
        double chAccBalance = 0;
        double svAccBalance = 0;
        try {
            if (ch_acc_box.isSelected()){
                chAccBalance = Double.parseDouble(ch_amount_fld.getText().trim());
            }
            if (sv_acc_box.isSelected()){
                svAccBalance = Double.parseDouble(sv_amount_fld.getText().trim());
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        //Kiểm tra dữ liệu
        if (firstName.isEmpty() || lastName.isEmpty()){
            showAlert("Please enter valid FirstName or LastName.");
        }else if (password.isEmpty()){
            showAlert("Please enter password.");
        } else if (chAccBalance < 0 || svAccBalance < 0){
            showAlert("Please enter valid Balance.");
        } else {
            // Thêm client mới
            String CheckingNumber = "3021 " + RanDomNumber();
            String SavingNumber = "3021 " + RanDomNumber();
            CheckingAccount checkingAccount = new CheckingAccount(pAddress , CheckingNumber , chAccBalance ,10);
            SavingAccount savingAccount = new SavingAccount(pAddress , SavingNumber , svAccBalance , 2000);
            Client client = new Client(firstName , lastName ,
                    pAddress , password ,
                    checkingAccount,
                    savingAccount,
                    LocalDate.now().toString());
            try {
                Model.getInstance().getDatabaseDriver().insertClient(client);
                ResultSet resultSet = Model.getInstance().getDatabaseDriver().getClientsData();
                while (resultSet.next()){
                    if (resultSet.getString("PayeeAddress").equals(pAddress)){
                        error_lbl.setText("Client Create Successfully.");
                        error_lbl.setTextFill(Color.BLUE);
                        Model.getInstance().getDatabaseDriver().insertCheckingAccount(checkingAccount);
                        Model.getInstance().getDatabaseDriver().insertSavingAccount(savingAccount);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                showAlert("Error adding client. Please try again.");
            }
        }
    }
    private String RanDomAddress(String firstName , String lastName){
        Random random = new Random();
        int ranDomNumber = random.nextInt(100);
        return "@" + Character.toUpperCase(firstName.charAt(0)) + lastName.toLowerCase() + ranDomNumber;
    }
    public int RanDomNumber(){
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshLabel(){
        fName__fld.setText("");
        lName_fld.setText("");
        password_fld.setText("");
        pAddress_box.setSelected(false);
        pAddress_lbl.setText("");
        ch_acc_box.setSelected(false);
        ch_amount_fld.setText("");
        sv_acc_box.setSelected(false);
        sv_amount_fld.setText("");
        error_lbl.setText("");

        lNameEdit_fld.setText("");
        passwordEdit_fld.setText("");
        pAddressEdit_fld.setText("");
        fNameEdit_fld.setText("");
    }
}
