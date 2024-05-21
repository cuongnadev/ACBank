package com.example.javafx.View;

import com.example.javafx.Controller.Admin.SignUpCellController;
import com.example.javafx.Models.SignUp;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class SignUpCellFactory extends ListCell<SignUp> {
    @Override
    protected void updateItem(SignUp signUp, boolean empty) {
        super.updateItem(signUp, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/SignUpCell.fxml"));
            SignUpCellController signUpCellController = new SignUpCellController(signUp);
            loader.setController(signUpCellController);

            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
