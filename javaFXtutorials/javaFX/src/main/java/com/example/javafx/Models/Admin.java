package com.example.javafx.Models;


import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "admins")
public class Admin {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int Id;
    @Column(name = "Username")
    private String UserName;
    @Column(name = "Password")
    private String Password;

    public Admin() {
    }

    public Admin (String username , String password){
        this.UserName = username;
        this.Password = password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
