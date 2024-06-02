package com.example.javafx.Models;


import jakarta.persistence.*;

@Entity
@Table (name = "clients")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "PayeeAddress")
    private String payeeAddress;
    @Column(name = "Password")
    private String password;
    @Column(name = "Date")
    private String dateCreated;

    @Column(name = "AdminName")
    private String adminName;



    public Clients() {
    }
    public Clients(String firstName, String lastName, String payeeAddress, String password, String dateCreated, String adminName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.payeeAddress = payeeAddress;
        this.password = password;
        this.dateCreated = dateCreated;
        this.adminName = adminName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(String payeeAddress) {
        this.payeeAddress = payeeAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
