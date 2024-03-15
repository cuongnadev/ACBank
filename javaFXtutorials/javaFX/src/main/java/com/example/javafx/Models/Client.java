package com.example.javafx.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty payeeAddress;
    private final StringProperty password;
    private final ObjectProperty<Account> checkingAccount;
    private final ObjectProperty<Account> savingAccount;
    private final StringProperty dateCreated;


    public Client (String fName , String lName , String pAddress , String pasword, Account cAccount , Account sAccount , String date){
        this.firstName = new SimpleStringProperty(this , "FirstName" , fName);
        this.lastName = new SimpleStringProperty(this , "LastName" , lName);
        this.payeeAddress = new SimpleStringProperty(this , "PayeeAddress" , pAddress);
        this.password = new SimpleStringProperty(this , "Password" , pasword);
        this.checkingAccount = new SimpleObjectProperty<>(this, "CheckingAccounts", cAccount);
        this.savingAccount = new SimpleObjectProperty<>(this , "SavingsAccounts" , sAccount);
        this.dateCreated = new SimpleStringProperty(this , "Date" , date);
    }

    public StringProperty firstNameProperty(){
        return this.firstName;
    }
    public StringProperty lastNameProperty(){
        return this.lastName;
    }
    public StringProperty pAddressProperty(){
        return this.payeeAddress;
    }
    public StringProperty passwordProperty(){return this.password;}
    public ObjectProperty<Account> checkingAccountProperty(){
        return this.checkingAccount;
    }
    public ObjectProperty<Account> savingAccountProperty(){
        return this.savingAccount;
    }
    public StringProperty dateCreatedProperty(){
        return this.dateCreated;
    }
}
