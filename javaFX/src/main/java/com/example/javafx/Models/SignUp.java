package com.example.javafx.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class SignUp {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty password;
    private final StringProperty pAddress;
    private final DoubleProperty chAccBalance;
    private final DoubleProperty svAccBalance;
    private final StringProperty date;
    private final StringProperty CheckingNumber;
    private final StringProperty SavingNumber;

    public SignUp(String firstName ,String  lastName , String password , String pAddress , double chAccBalance ,
                  double svAccBalance , String date , String CheckingNumber ,String SavingNumber) {
        this.firstName = new SimpleStringProperty(this , "FirstName" , firstName);
        this.lastName = new SimpleStringProperty(this , "LastName" , lastName);
        this.password = new SimpleStringProperty(this , "Password" , password);
        this.pAddress = new SimpleStringProperty(this , "PayeeAddress" , pAddress);
        this.chAccBalance  = new SimpleDoubleProperty(this , "CheckingAmount" , chAccBalance);
        this.svAccBalance  = new SimpleDoubleProperty(this , "SavingAmount" , svAccBalance);
        this.date = new SimpleStringProperty(this , "Date" , date);
        this.CheckingNumber  = new SimpleStringProperty(this , "CheckingNumber" , CheckingNumber);
        this.SavingNumber  = new SimpleStringProperty(this , "SavingNumber" , SavingNumber);

    }

    public StringProperty FirstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty PasswordProperty() {
        return password;
    }

    public StringProperty pAddressProperty() {
        return pAddress;
    }

    public DoubleProperty chAccBalanceProperty() {
        return chAccBalance;
    }

    public DoubleProperty svAccBalanceProperty() {
        return svAccBalance;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty checkingNumberProperty() {
        return CheckingNumber;
    }

    public StringProperty savingNumberProperty() {
        return SavingNumber;
    }
}
