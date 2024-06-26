package com.example.javafx.View;

import com.example.javafx.Controller.Client.TransactionCellController;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class TransactionCellFactory extends ListCell<Transaction> {
    private String clientId;

    public TransactionCellFactory(String clientId) {
        this.clientId = clientId;
    }

    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction , empty);
        if (empty){
            setText(null);
            setGraphic(null);
        } else {
            TransactionCellController controller = new TransactionCellController(transaction, clientId);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Client/TransactionCell.fxml"));
            loader.setController(controller);

            setText(null);
            try {
                setGraphic(loader.load());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
