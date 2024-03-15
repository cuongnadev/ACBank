package com.example.javafx.Controller.GetView;

import com.example.javafx.Controller.Admin.ReceiptCellController;
import com.example.javafx.Models.Receipt;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ReceiptCellFactory extends ListCell<Receipt> {
    @Override
    protected void updateItem(Receipt receipt, boolean empty) {
        super.updateItem(receipt, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/ReceiptCell.fxml"));
            ReceiptCellController controller = new ReceiptCellController(receipt);
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
