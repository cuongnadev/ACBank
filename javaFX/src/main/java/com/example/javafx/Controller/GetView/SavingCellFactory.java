package com.example.javafx.Controller.GetView;

import com.example.javafx.Controller.Admin.SavingCellController;
import com.example.javafx.Models.SavingAccount;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class SavingCellFactory extends ListCell<SavingAccount> {
    @Override
    protected void updateItem(SavingAccount savingAccount, boolean empty) {
        super.updateItem(savingAccount, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/SavingCell.fxml"));
            SavingCellController savingCellController = new SavingCellController(savingAccount);
            loader.setController(savingCellController);

            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }
}
