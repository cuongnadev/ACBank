package com.example.javafx.Client;

import com.example.javafx.Models.Model;
import com.example.javafx.View.ClientMenuOptions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ClientSession {
    private String clientId;
    private ObjectProperty<ClientMenuOptions> selectedMenuItem;

    public ClientSession(String clientId) {
        this.clientId = clientId;
        this.selectedMenuItem = new SimpleObjectProperty<>(ClientMenuOptions.DASHBOARD);
    }

    public String getClientId() {
        return clientId;
    }

    public ObjectProperty<ClientMenuOptions> selectedMenuItemProperty() {
        return selectedMenuItem;
    }

    public void setSelectedMenuItem(ClientMenuOptions selectedMenuItem) {
        this.selectedMenuItem.set(selectedMenuItem);
    }
}
