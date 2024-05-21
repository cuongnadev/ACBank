package com.example.javafx.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "signupaccount")
public class SignUp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int Id;
    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "Password")
    private String password;
    @Column(name = "PayeeAddress")
    private String pAddress;
    @Column(name = "CheckingAmount")
    private Double chAccBalance;
    @Column(name = "SavingAmount")
    private Double svAccBalance;
    @Column(name = "Date")
    private String date;
    @Column(name = "CheckingNumber")
    private String CheckingNumber;
    @Column(name = "SavingNumber")
    private String SavingNumber;

    public SignUp() {
    }

    public SignUp(String firstName, String lastName, String password, String pAddress, Double chAccBalance, Double svAccBalance, String date, String checkingNumber, String savingNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.pAddress = pAddress;
        this.chAccBalance = chAccBalance;
        this.svAccBalance = svAccBalance;
        this.date = date;
        CheckingNumber = checkingNumber;
        SavingNumber = savingNumber;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public Double getChAccBalance() {
        return chAccBalance;
    }

    public void setChAccBalance(Double chAccBalance) {
        this.chAccBalance = chAccBalance;
    }

    public Double getSvAccBalance() {
        return svAccBalance;
    }

    public void setSvAccBalance(Double svAccBalance) {
        this.svAccBalance = svAccBalance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckingNumber() {
        return CheckingNumber;
    }

    public void setCheckingNumber(String checkingNumber) {
        CheckingNumber = checkingNumber;
    }

    public String getSavingNumber() {
        return SavingNumber;
    }

    public void setSavingNumber(String savingNumber) {
        SavingNumber = savingNumber;
    }
}
