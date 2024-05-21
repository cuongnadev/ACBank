package com.example.javafx.Models;

import jakarta.persistence.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "forgotpass")
public class ForgotPass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int Id;
    @Column(name = "PayeeAddress")
    private String pAddress;
    @Column(name = "Date")
    private String date;
    @Column(name = "Email")
    private String email;

    public ForgotPass() {
    }

    public ForgotPass (String pAddress , String date , String email){
        this.pAddress = pAddress;
        this.date = date;
        this.email = email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
