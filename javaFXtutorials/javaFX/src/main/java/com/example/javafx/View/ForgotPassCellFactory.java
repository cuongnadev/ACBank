package com.example.javafx.View;


import com.example.javafx.Controller.Admin.ForgotPassCellController;

import com.example.javafx.Models.ForgotPass;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.ListCell;

public class ForgotPassCellFactory extends ListCell<ForgotPass> {

    @Override
    protected void updateItem(ForgotPass forgotPass, boolean empty) {
        super.updateItem(forgotPass, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/ForgotPassCell.fxml"));
            ForgotPassCellController controller = new ForgotPassCellController(forgotPass);
            loader.setController(controller);

            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
