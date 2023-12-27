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
        try {
            statement = this.con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress='"+pAddress+"' AND Password='"+password+"';");
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

    public void updateSavingBalance (String owner, double newBalance) {
        String updateQuery = "update SavingsAccounts set Balance = ? where Owner = ?;";

        try {
            PreparedStatement pstm = con.prepareStatement(updateQuery);

            pstm.setDouble(1, newBalance);
            pstm.setString(2, owner);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No account found for owner: " + owner);
            } else {
                System.out.println("Saving balance updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating saving balance.");
        }
    }



    //Insert
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

    // Drop

    public void DropClient (String pAddress ){
        String insertQuery = "DELETE FROM Clients WHERE PayeeAddress= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
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
        String insertQuery = "DELETE FROM CheckingAccounts WHERE Owner= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
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
        String insertQuery = "DELETE FROM SavingsAccounts WHERE Owner= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
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
        String insertQuery = "DELETE FROM Transactions WHERE Sender= ? or  Receiver= ?";
        try {
            PreparedStatement pstm = con.prepareStatement(insertQuery);
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
}