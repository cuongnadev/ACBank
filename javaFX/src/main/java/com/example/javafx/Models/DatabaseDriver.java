package com.example.javafx.Models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDriver {
    Connection con;
    public DatabaseDriver() {
        try {
            this.con = DriverManager.getConnection("jdbc:sqlite:acbank.db");
            //test connected
            System.out.println("Connected");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*
    * Client Select
    * */
    public ResultSet getClientData (String pAddress , String password){
        Statement statement;
        ResultSet resultSet = null;
        String pword = Model.HashPassword(password);
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='"+pAddress+"' AND Password='"+pword+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getClientsData (){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }


    /*
    Admin Seclect
    * */
    public ResultSet getAdminData (String username , String password){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Admins WHERE Username='"+username+"' AND Password='"+password+"';");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    /*
    * Transactions Seclect
    * */
    public ResultSet getTransactionData (){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Transactions");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }


    /*
    * Checking Account Seclect
    * */
    public ResultSet getChekingAccountsData (){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    /*
    * Saving Account Seclect
    * */
    public ResultSet getSavingAccountsData (){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    //Receipt select
    public ResultSet getReceiptData(){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Receipt");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    //SignUp select
    public ResultSet getSignUpAccountData(){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SignUpAccount");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }

    //ForgotPass select
    public ResultSet getForgotPass (){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ForgotPass");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }



    //Update
    public void updateFNameClients (String pAddress ,String name){
        String updateQuery = "update Clients set FirstName  = ? where PayeeAddress = ?;";

        try {
            PreparedStatement pstm = con.prepareStatement(updateQuery);

            pstm.setString(1, name);
            pstm.setString(2, pAddress);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No account found for Payee Address: " + pAddress);
            } else {
                System.out.println("First Name updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating First Name.");
        }
    }
    public void updateLNameClients (String pAddress ,String name){
        String updateQuery = "update Clients set LastName  = ? where PayeeAddress = ?;";

        try {
            PreparedStatement pstm = con.prepareStatement(updateQuery);

            pstm.setString(1, name);
            pstm.setString(2, pAddress);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No account found for Payee Address: " + pAddress);
            } else {
                System.out.println("Last Name updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating Last Name.");
        }
    }
    public void updatepasswordClients (String pAddress ,String password){
        String updateQuery = "update Clients set Password  = ? where PayeeAddress = ?;";

        try {
            PreparedStatement pstm = con.prepareStatement(updateQuery);

            pstm.setString(1, password);
            pstm.setString(2, pAddress);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No account found for Payee Address: " + pAddress);
            } else {
                System.out.println("Password updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating Password.");
        }
    }
    public void updateAccountBalance (String owner, double newBalance) {
        String updateQuery = "update CheckingAccounts set Balance = ? where Owner = ?;";

        try {
            PreparedStatement pstm = con.prepareStatement(updateQuery);

            pstm.setDouble(1, newBalance);
            pstm.setString(2, owner);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No account found for owner: " + owner);
            } else {
                System.out.println("Checking balance updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating checking balance.");
        }
    }
    public void updateSavingBalance (String sav_Num, double newBalance) {
        String updateQuery = "update SavingsAccounts set Balance = ? where AccountNumber = ?;";

        try {
            PreparedStatement pstm = con.prepareStatement(updateQuery);

            pstm.setDouble(1, newBalance);
            pstm.setString(2, sav_Num);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No account found for SavingNumber: " + sav_Num);
            } else {
                System.out.println("Saving balance updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating saving balance.");
        }
    }



    //Insert
    public void insertForgotPass(ForgotPass forgotPass) {
        String insertQuery = "INSERT INTO ForgotPass (PayeeAddress, Date , Email) VALUES (?, ?, ?);";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);

            pstm.setString(1, forgotPass.pAddressProperty().get());
            pstm.setString(2, forgotPass.dateProperty().get());
            pstm.setString(3,forgotPass.emailProperty().get());


            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("ForgotPass inserted successfully.");
            } else {
                System.out.println("Failed to insert ForgotPass.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting ForgotPass.");
        }
    }
    public void insertTransaction(Transaction transaction) {
        String insertQuery = "INSERT INTO Transactions (Sender, Receiver, Amount, Date, Message) VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
            pstm.setDouble(3, transaction.amountProperty().get());
            pstm.setString(1, transaction.senderProperty().get());
            pstm.setString(2, transaction.receiverProperty().get());
            pstm.setString(4, transaction.dateProperty().get());
            pstm.setString(5, transaction.messageProperty().get());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction inserted successfully.");
            } else {
                System.out.println("Failed to insert transaction.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting transaction.");
        }
    }
    public void insertClient(Client client){
        String insertQuery = "INSERT INTO Clients (FirstName , LastName , PayeeAddress , Password , Date) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
            pstm.setString(1, client.firstNameProperty().get());
            pstm.setString(2, client.lastNameProperty().get());
            pstm.setString(3, client.pAddressProperty().get());
            pstm.setString(4, client.passwordProperty().get());
            pstm.setString(5, client.dateCreatedProperty().get());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Client inserted successfully.");
            } else {
                System.out.println("Failed to insert client.");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error inserting client.");
        }
    }
    public void insertCheckingAccount(CheckingAccount checkingAccount){
        String insertQuery = "INSERT INTO CheckingAccounts (Owner , AccountNumber , TransactionLimit , Balance) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
            pstm.setString(1, checkingAccount.ownerProperty().get());
            pstm.setString(2, checkingAccount.accountNumberPropperty().get());
            pstm.setDouble(3, checkingAccount.transactionLimitProperty().get());
            pstm.setDouble(4, checkingAccount.balanceProperty().get());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("CheckingAccount inserted successfully.");
            } else {
                System.out.println("Failed to insert CheckingAccount.");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error inserting checkingAccount.");
        }
    }
    public void insertSavingAccount(SavingAccount savingAccount){
        String insertQuery = "INSERT INTO SavingsAccounts (Owner , AccountNumber , WithdrawalLimit , Balance) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
            pstm.setString(1, savingAccount.ownerProperty().get());
            pstm.setString(2, savingAccount.accountNumberPropperty().get());
            pstm.setDouble(3, savingAccount.withdrawalLimitProperty().get());
            pstm.setDouble(4, savingAccount.balanceProperty().get());

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("SavingAccount inserted successfully.");
            } else {
                System.out.println("Failed to insert SavingAccount.");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error inserting savingAccount.");
        }
    }
    public void insertReceiver (String IDBienLai , String sender , String receiver , String numberSender , String numberReceiver , double amount , String date , String message ){
        String insertQuery = "INSERT INTO Receipt (IDBienLai , Sender , Receiver , NumberSender , NumberReceiver , Amount , Date , Message ) VALUES (?, ?, ?, ?, ?, ?, ? ,?)";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
            pstm.setString(1, IDBienLai);
            pstm.setString(2, sender);
            pstm.setString(3, receiver);
            pstm.setString(4, numberSender);
            pstm.setString(5, numberReceiver);
            pstm.setDouble(6, amount);
            pstm.setString(7, date);
            pstm.setString(8, message);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Receipt inserted successfully.");
            } else {
                System.out.println("Failed to insert receipt.");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error inserting receipt.");
        }
    }
    public void insertSignUp (String FirstName , String LastName , String Password , String PayeeAddress , Double CheckingAmount , Double SavingAmount , String Date , String CheckingNumber , String SavingNumber){
        String insertQuery = "INSERT INTO SignUpAccount (FirstName , LastName , Password , PayeeAddress , CheckingAmount , SavingAmount , Date , CheckingNumber , SavingNumber) VALUES (? ,? ,? ,? ,? , ?, ?, ? ,?)";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
            pstm.setString(1, FirstName);
            pstm.setString(2, LastName);
            pstm.setString(3, Password);
            pstm.setString(4, PayeeAddress);
            pstm.setDouble(5, CheckingAmount);
            pstm.setDouble(6, SavingAmount);
            pstm.setString(7, Date);
            pstm.setString(8, CheckingNumber);
            pstm.setString(9, SavingNumber);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("SignUpAccount inserted successfully.");
            } else {
                System.out.println("Failed to insert SignUpAccount.");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error inserting SignUpAccount.");
        }
    }



    // Drop
    public void DropForgotPass (String pAddress ){
        String dropQuery = "DELETE FROM ForgotPass WHERE PayeeAddress= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(dropQuery);
            pstm.setString(1 , pAddress);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("ForgotPass Drop successfully.");
            } else {
                System.out.println("Failed to Drop ForgotPass.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void DropSignUpAccount (String pAddress ){
        String dropQuery = "DELETE FROM SignUpAccount WHERE PayeeAddress= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(dropQuery);
            pstm.setString(1 , pAddress);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("SignUp Drop successfully.");
            } else {
                System.out.println("Failed to Drop SignUp.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void DropClient (String pAddress ){
        String dropQuery = "DELETE FROM Clients WHERE PayeeAddress= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(dropQuery);
            pstm.setString(1 , pAddress);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Client Drop successfully.");
            } else {
                System.out.println("Failed to Drop Client.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void DropCheckingAccount (String pAddress ){
        String dropQuery = "DELETE FROM CheckingAccounts WHERE Owner= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(dropQuery);
            pstm.setString(1 , pAddress);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("CheckingAccount Drop successfully.");
            } else {
                System.out.println("Failed to Drop CheckingAccount.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void DropSavingAccount (String pAddress ){
        String dropQuery = "DELETE FROM SavingsAccounts WHERE Owner= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(dropQuery);
            pstm.setString(1 , pAddress);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("SavingAccount Drop successfully.");
            } else {
                System.out.println("Failed to Drop SavingAccount.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void DropTransaction (String pAddress ){
        String dropQuery = "DELETE FROM Transactions WHERE Sender= ? or  Receiver= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(dropQuery);
            pstm.setString(1 , pAddress);
            pstm.setString(2 ,pAddress);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transaction Drop successfully.");
            } else {
                System.out.println("Failed to Drop Transaction.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void DropReceipt (String IDBienLai ){
        String dropQuery = "DELETE FROM Receipt WHERE IDBienLai= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(dropQuery);
            pstm.setString(1 , IDBienLai);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Receipt Drop successfully.");
            } else {
                System.out.println("Failed to Drop Receipt.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
//ok...