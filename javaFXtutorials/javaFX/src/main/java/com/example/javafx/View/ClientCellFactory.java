package com.example.javafx.View;

import com.example.javafx.Controller.Admin.ClientCellController;
import com.example.javafx.Models.Clients;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ClientCellFactory extends ListCell<Clients> {
    @Override
    protected void updateItem(Clients client, boolean empty) {
        super.updateItem(client, empty);
        if(empty){
            setText(null);
            setGraphic(null);
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/ClientCell.fxml"));
            ClientCellController controller = new ClientCellController(client);
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
