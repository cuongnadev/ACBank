package com.example.javafx.Models;

import com.example.javafx.Dao.AdminDao;
import com.example.javafx.Dao.ClientsDao;
import com.example.javafx.Dao.DaoDriver;
import com.example.javafx.View.AccountType;
import com.example.javafx.View.ViewFactory;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DaoDriver daoDriver;


    //Client Data Section
    private final Clients myClient;
    private Boolean clientLoginSuccessFlag;


    //Admin Data Section
    private final Admin admin;
    private Boolean adminLoginSuccessFlag;

    private Model (){
        this.daoDriver = new DaoDriver();
        this.viewFactory = new ViewFactory();
        //Client Data Section
        this.clientLoginSuccessFlag = false;
        this.myClient = new Clients("", "", "", "","");
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
    public DaoDriver getDaoDriver() {
        return daoDriver;
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
    public Clients getClients() {
        return myClient;
    }
    public void evaluateClientCred(String pAddress , String password){
        List<Clients> clientsList = this.getDaoDriver().getClientsDao().getAllClients();
        try {

            for (Clients client : clientsList) {
                if(client.getPayeeAddress().equals(pAddress) && client.getPassword().equals(HashPassword(password))) {
                    this.myClient.setId(client.getId());
                    this.myClient.setFirstName(client.getFirstName());
                    this.myClient.setLastName(client.getLastName());
                    this.myClient.setPayeeAddress(client.getPayeeAddress());
                    this.myClient.setPassword(password);
                    this.myClient.setDateCreated(client.getDateCreated());
                    this.setClientLoginSuccessFlag(true);
                    break;
                }
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
        List<Admin> adminList = this.getDaoDriver().getAdminDao().getAllAdmins();
        try {
            for(Admin admin1 : adminList) {
                if(admin1.getUserName().equals(username) && admin1.getPassword().equals(password)) {
                    this.adminLoginSuccessFlag = true;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // hash Password
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
