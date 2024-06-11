package com.example.javafx;

import com.example.javafx.Models.Model;
import com.example.javafx.Server.BankServer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
