package com.example.javafx.Models;

import com.example.javafx.Controller.GetView.AccountType;
import com.example.javafx.Controller.GetView.ViewFactory;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private AccountType loginAccountType = AccountType.CLIENT;
    //Client Data Section
    private final Client client;
    private Boolean clientLoginSuccessFlag;
    //Admin Data Section
    private Admin admin;
    private Boolean adminLoginSuccessFlag;

    private Model (){
        this.databaseDriver = new DatabaseDriver();
        this.viewFactory = new ViewFactory();
        //Client Data Section
        this.clientLoginSuccessFlag = false;
        this.client = new Client("" , "" ,"" , null , null , null , null);
        //Admin Data Section
        this.adminLoginSuccessFlag = false ;
        this.admin = new Admin("" ,"");
    }
    public static synchronized Model getInstance(){
        if (model  == null){
            model = new Model();
        }
        return model;
    }
    public ViewFactory getViewFactory(){
        return viewFactory;
    }
    public DatabaseDriver getDatabaseDriver(){
        return databaseDriver;
    }
    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }


    /*
    * Client Method Section
    * */
    public boolean getClientLoginSuccessFlag(){
        return this.clientLoginSuccessFlag;
    }
    public void setClientLoginSuccessFlag (boolean flag){
        this.clientLoginSuccessFlag = flag;
    }
    public Client getClient() {
        return client;
    }
    public void evaluateClientCred(String pAddress , String password){
        ResultSet resultSet = databaseDriver.getClientData(pAddress , password);
        try {
            if(resultSet.isBeforeFirst()){
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.pAddressProperty().set(pAddress);
                this.client.passwordProperty().set(password);
                String[] dataParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dataParts[0]) , Integer.parseInt(dataParts[1]) , Integer.parseInt(dataParts[2]));
                this.client.dateCreatedProperty().set(String.valueOf(date));
                this.clientLoginSuccessFlag = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
    * Admin Method Section
    * */
    public Boolean getAdminLoginSuccessFlag() {
        return adminLoginSuccessFlag;
    }
    public void setAdminLoginSuccessFlag(Boolean Flag) {
        this.adminLoginSuccessFlag = Flag;
    }
    public Admin getAdmin(){
        return admin;
    }
    public void evaluateAdminCred(String username , String password){
        ResultSet resultSet = databaseDriver.getAdminData(username , password);
        try {
            if(resultSet.isBeforeFirst()){
                this.admin.userNameProperty().set(resultSet.getString("Username"));
                this.admin.passwordProperty().set(resultSet.getString("Password"));
                this.adminLoginSuccessFlag = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String HashPassword(String password){
        try {
            MessageDigest pass = MessageDigest.getInstance("MD5");
            pass.update(password.getBytes());
            byte[] hashedBytes = pass.digest();


            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
