package com.example.javafx.Models;

import javafx.beans.property.*;

public class Receipt {
    private final StringProperty IDReceipt;
    private final StringProperty sender;
    private final StringProperty receiver;
    private final DoubleProperty amount;

    private final StringProperty date;

    public Receipt(String IDReceipt, String sender, String receiver, Double amount, String date) {
        this.IDReceipt = new SimpleStringProperty(this , "IDReceipt" , IDReceipt);
        this.sender = new SimpleStringProperty(this , "Sender" , sender);
        this.receiver = new SimpleStringProperty(this , "Receiver" , receiver);
        this.amount = new SimpleDoubleProperty(this , "Amount" , amount);
        this.date = new SimpleStringProperty(this , "Date" , date);
    }

    public StringProperty IDReceiptProperty(){
        return this.IDReceipt;
    }
    public StringProperty senderProperty(){
        return this.sender;
    }
    public StringProperty recerverProperty(){
        return this.receiver;
    }
    public DoubleProperty amountProperty(){return this.amount;}

    public StringProperty dateProperty(){
        return this.date;
    }
}
