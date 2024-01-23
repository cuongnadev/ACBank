package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Model;
import com.example.javafx.Models.SignUp;
import com.example.javafx.Controller.GetView.SignUpCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SignUpListController implements Initializable {


    public ListView signUp_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<SignUp> receipts = getSignUpOfSQLite();
        signUp_listview.getItems().addAll(receipts);
        signUp_listview.setCellFactory(listView -> new SignUpCellFactory());
        Model.getInstance().getViewFactory().setSignUpListController(this);
    }

    public List<SignUp> getSignUpOfSQLite() {
        signUp_listview.getItems().clear();
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getSignUpAccountData();

        List<SignUp> signUpList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                SignUp signUp = new SignUp(
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Password"),
                        resultSet.getString("PayeeAddress"),
                        resultSet.getDouble("CheckingAmount"),
                        resultSet.getDouble("SavingAmount"),
                        resultSet.getString("Date"),
                        resultSet.getString("CheckingNumber"),
                        resultSet.getString("SavingNumber"));

                signUpList.add(signUp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Sắp xếp danh sách theo ngày giảm dần
        signUpList.sort((t1, t2) -> t2.dateProperty().get().compareTo(t1.dateProperty().get()));
        return signUpList;
    }
    public void refreshSignUpListView() {
        signUp_listview.getItems().clear();
        List<SignUp> signUps = getSignUpOfSQLite();
        signUp_listview.getItems().setAll(signUps);
    }
}
