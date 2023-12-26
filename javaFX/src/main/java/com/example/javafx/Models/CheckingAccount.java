package com.example.javafx.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

public class CheckingAccount extends Account{
    // The number of transaction a client is allowed to do per day
    private final IntegerProperty transactionLimit;

    public CheckingAccount(String owner , String accountNumber , double balance , int tLimit){
        super(owner , accountNumber , balance);
        this.transactionLimit = new SimpleIntegerProperty(this , "TransactionLimit" , tLimit) ;
    }

    public IntegerProperty transactionLimitProperty (){
        return this.transactionLimit;
    }
}
