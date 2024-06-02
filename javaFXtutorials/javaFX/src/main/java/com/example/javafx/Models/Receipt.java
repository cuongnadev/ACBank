package com.example.javafx.Models;


import jakarta.persistence.*;

@Entity
@Table(name = "receipt")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int Id;
    @Column(name = "IDBienLai")
    private String IDReceipt;
    @Column(name = "Sender")
    private String sender;
    @Column(name = "Receiver")
    private String receiver;
    @Column(name = "NumberSender")
    private String numberSender;
    @Column(name = "NumberReceiver")
    private String numberReceiver;
    @Column(name = "Amount")
    private Double amount;
    @Column(name = "Date")
    private String date;
    @Column(name = "Message")
    private String message;
    @Column(name = "AdminName")
    private String adminName;

    public Receipt() {
    }

    public Receipt(String IDReceipt, String sender, String receiver, String numberSender, String numberReceiver, Double amount, String date, String message, String adminName) {
        this.IDReceipt = IDReceipt;
        this.sender = sender;
        this.receiver = receiver;
        this.numberSender = numberSender;
        this.numberReceiver = numberReceiver;
        this.amount = amount;
        this.date = date;
        this.message = message;
        this.adminName = adminName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIDReceipt() {
        return IDReceipt;
    }

    public void setIDReceipt(String IDReceipt) {
        this.IDReceipt = IDReceipt;
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

    public String getNumberSender() {
        return numberSender;
    }

    public void setNumberSender(String numberSender) {
        this.numberSender = numberSender;
    }

    public String getNumberReceiver() {
        return numberReceiver;
    }

    public void setNumberReceiver(String numberReceiver) {
        this.numberReceiver = numberReceiver;
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

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
