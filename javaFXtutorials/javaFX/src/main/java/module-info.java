module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires kernel;
    requires layout;
    requires io;
    requires java.mail;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    exports com.example.javafx;
    opens com.example.javafx to javafx.fxml;
    opens com.example.javafx.Models to org.hibernate.orm.core;
    exports com.example.javafx.Controller;
    exports com.example.javafx.Controller.Client;
    exports com.example.javafx.Controller.Admin;
    exports com.example.javafx.Models;
    exports com.example.javafx.View;

}