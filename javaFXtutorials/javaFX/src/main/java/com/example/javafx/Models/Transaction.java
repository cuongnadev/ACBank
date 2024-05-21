package com.example.javafx.Models;

import jakarta.persistence.*;
import javafx.beans.property.*;

import java.time.LocalDate;
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int Id;
    @Column(name = "Sender")
    private  String sender;
    @Column(name = "Receiver")
    private  String receiver;
    @Column(name = "Amount")
    private  Double amount;
    @Column(name = "Date")
    private  String date;
    @Column(name = "Message")
    private  String message;

    public Transaction() {
    }

    public Transaction (String sender , String receiver , double amount , String date , String message){
        this.sender = sender;
        this.receiver = receiver;
        this.amount =  amount;
        this.date =  date;
        this.message =  message;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
