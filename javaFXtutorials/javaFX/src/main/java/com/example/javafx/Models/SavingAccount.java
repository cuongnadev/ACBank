package com.example.javafx.Models;

import jakarta.persistence.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;


@Entity
@Table(name = "savingaccounts")
public class SavingAccount {
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
    @Column(name = "WithdrawalLimit")
    private Integer withdrawalLimit;

    public SavingAccount() {
    }

    public SavingAccount(String owner, String accountNumber, Double balance, Integer withdrawalLimit) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.withdrawalLimit = withdrawalLimit;
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

    public Integer getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public void setWithdrawalLimit(Integer withdrawalLimit) {
        this.withdrawalLimit = withdrawalLimit;
    }
}
