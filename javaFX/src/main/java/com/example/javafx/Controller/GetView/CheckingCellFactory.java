package com.example.javafx.Controller.GetView;

import com.example.javafx.Controller.Admin.CheckingCellController;
import com.example.javafx.Models.CheckingAccount;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class CheckingCellFactory extends ListCell<CheckingAccount> {
    @Override
    protected void updateItem(CheckingAccount checkingAccount, boolean empty) {
        super.updateItem(checkingAccount, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Fxml/Admin/CheckingCell.fxml"));
            CheckingCellController checkingCellController = new CheckingCellController(checkingAccount);
            loader.setController(checkingCellController);

            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
