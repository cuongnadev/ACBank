package com.example.javafx.Models;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ForgotPass {
    private final StringProperty pAddress;
    private final StringProperty date;
    public ForgotPass ( String pAddress , String date){
        this.pAddress = new SimpleStringProperty(this , "PayeeAddress" , pAddress);
        this.date = new SimpleStringProperty(this , "Date" , date);
    }
    public StringProperty pAddressProperty(){
        return this.pAddress;
    }
    public StringProperty dateProperty(){
        return this.date;
    }
}
