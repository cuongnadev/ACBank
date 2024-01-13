module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires kernel;
    requires layout;

    requires java.mail;


    exports com.example.javafx;
    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx.Controller;
    exports com.example.javafx.Controller.Client;
    exports com.example.javafx.Controller.Admin;
    exports com.example.javafx.Models;
    exports com.example.javafx.View;

}