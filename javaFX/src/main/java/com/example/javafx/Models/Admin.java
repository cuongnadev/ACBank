package com.example.javafx.Models;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {
    private final StringProperty UserName;
    private final StringProperty Password;

    public Admin (String username , String password){
        this.UserName = new SimpleStringProperty(this , "Username" , username);
        this.Password = new SimpleStringProperty(this , "Password" , password);
    }

    public StringProperty userNameProperty(){
        return this.UserName;
    }
    public StringProperty passwordProperty(){
        return this.Password;
    }
}
