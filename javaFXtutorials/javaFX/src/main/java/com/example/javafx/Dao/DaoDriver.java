package com.example.javafx.Dao;

import com.example.javafx.Models.Clients;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.SavingAccount;

public class DaoDriver {
    private static DaoDriver daoDriver;
    private final AdminDao adminDao;
    private final ClientsDao clientsDao;
    private final CheckingAccountDao checkingAccountDao;
    private final ForgotpassDao forgotpassDao;
    private final ReceiptDao receiptDao;
    private final SavingAccountDao savingAccountDao;
    private final SignUpDao signUpDao;
    private final TransactionDao transactionDao;

    public DaoDriver() {
        this.adminDao = new AdminDao();
        this.clientsDao = new ClientsDao();
        this.checkingAccountDao = new CheckingAccountDao();
        this.forgotpassDao = new ForgotpassDao();
        this.receiptDao = new ReceiptDao();
        this.savingAccountDao = new SavingAccountDao();
        this.signUpDao = new SignUpDao();
        this.transactionDao = new TransactionDao();
    }

    public static synchronized DaoDriver getInstance(){
        if (daoDriver  == null){
            daoDriver = new DaoDriver();
        }
        return daoDriver;
    }

    public static DaoDriver getDaoDriver() {
        return daoDriver;
    }

    public AdminDao getAdminDao() {
        return adminDao;
    }

    public ClientsDao getClientsDao() {
        return clientsDao;
    }

    public CheckingAccountDao getCheckingAccountDao() {
        return checkingAccountDao;
    }

    public ForgotpassDao getForgotpassDao() {
        return forgotpassDao;
    }

    public ReceiptDao getReceiptDao() {
        return receiptDao;
    }

    public SavingAccountDao getSavingAccountDao() {
        return savingAccountDao;
    }

    public SignUpDao getSignUpDao() {
        return signUpDao;
    }

    public TransactionDao getTransactionDao() {
        return transactionDao;
    }
}
