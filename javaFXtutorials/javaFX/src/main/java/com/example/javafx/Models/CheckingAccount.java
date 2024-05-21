package com.example.javafx.Models;

import jakarta.persistence.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;


@Entity
@Table(name = "checkingaccounts")
public class CheckingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int Id;
    @Column(name = "Owner")
    private String owner;
    @Column(name = "AccountNumber")
    private String accountNumber;
    @Column(name = "Balance")
    private Double balance;
    @Column(name = "TransactionLimit")
    private Integer transactionLimit;

    public CheckingAccount() {
    }

    public CheckingAccount(String owner, String accountNumber, Double balance, Integer transactionLimit) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionLimit = transactionLimit;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(Integer transactionLimit) {
        this.transactionLimit = transactionLimit;
    }
}
