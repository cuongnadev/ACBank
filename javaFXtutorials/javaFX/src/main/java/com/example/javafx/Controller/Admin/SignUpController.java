package com.example.javafx.Controller.Admin;


import com.example.javafx.Models.Clients;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.SignUp;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
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




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> onCreate());
        pAddress_box.setOnAction(event -> onpAddressBox());
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
            pAddress_lbl.setText("@" + lName_fld.getText() + fName__fld.getText());
        }
    }

    private void onCreate() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        //Lấy dữ liệu đầu vào từ field
        String firstName = fName__fld.getText().trim();
        String lastName = lName_fld.getText().trim();
        String password = password_fld.getText().trim();
        String pAddress = pAddress_lbl.getText().trim();
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        double chAccBalance = 0;
        double svAccBalance = 0;
        try {
            if (ch_acc_box.isSelected()) {
                chAccBalance = Double.parseDouble(ch_amount_fld.getText().trim());
            }
            if (sv_acc_box.isSelected()) {
                svAccBalance = Double.parseDouble(sv_amount_fld.getText().trim());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //Kiểm tra dữ liệu
        for (Clients client : clientsList) {
            if (pAddress.equals(client.getPayeeAddress())) {
                showAlert("PayeeAddress already exists!");
                break;
            } else if (firstName.isEmpty() || lastName.isEmpty()) {
                showAlert("Please enter valid FirstName or LastName.");
                break;
            } else if (password.isEmpty()) {
                showAlert("Please enter password.");
                break;
            } else if (chAccBalance < 0 || svAccBalance < 0) {
                showAlert("Please enter valid Balance.");
                break;
            } else {
                String nameAdmin = Model.getInstance().getAdmin().getUserName();
                // Thêm client mới vào hàng chờ chờ admin duyệt
                String CheckingNumber = "3021 " + RanDomNumber();
                String SavingNumber = "3021 " + RanDomNumber();
                String pword = Model.HashPassword(password);
                SignUp newSignUp = new SignUp(firstName, lastName, pword, pAddress, chAccBalance,
                        svAccBalance, LocalDate.now().toString(), CheckingNumber, SavingNumber, nameAdmin);
                Model.getInstance().getDaoDriver().getSignUpDao().saveSignUp(newSignUp);
                error_lbl.setText("Client Create Successfully.");
                error_lbl.setTextFill(Color.BLUE);
                showAlertSuccessful("Account created successfully, please wait for admin approval");

                //Close the SignUp window
                Model.getInstance().getViewFactory().closeStage(stage);
                Model.getInstance().getViewFactory().getSignUpListController().refreshSignUpListView();
                break;
            }
        }
    }

    private String RanDomAddress(String firstName , String lastName){
        Random random = new Random();
        int ranDomNumber = random.nextInt(100);
        return  Character.toUpperCase(firstName.charAt(0)) + lastName.toLowerCase() + "@" + ranDomNumber;
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

    public void showAlertSuccessful(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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

    }
}
