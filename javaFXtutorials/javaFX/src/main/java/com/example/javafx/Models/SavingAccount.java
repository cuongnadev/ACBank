package com.example.javafx.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SavingAccount extends Account{
    // The withdrawal limit from the saving
    private final DoubleProperty withdrawalLimit;

    public SavingAccount(String owner , String accountNumber , double balance , int withdrawalLimit){
        super(owner , accountNumber , balance);
        this.withdrawalLimit = new SimpleDoubleProperty(this , "WithdrawalLimit" , withdrawalLimit) ;
    }

    public DoubleProperty withdrawalLimitProperty (){
        return this.withdrawalLimit;
    }
}
