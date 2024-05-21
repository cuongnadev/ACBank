package com.example.javafx.Dao;

import com.example.javafx.Models.Admin;
import com.example.javafx.Models.CheckingAccount;
import com.example.javafx.Models.Clients;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CheckingAccountDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save Checking Account
    public void saveCheckingAccount(CheckingAccount checkingAccount) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save Checking Account object
            session.save(checkingAccount);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }


    //get All Checking Account
    public List<CheckingAccount> getAllCheckingAccounts() {
        Transaction transaction = null;
        List<CheckingAccount> checkingAccountList = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get Checking Account object
            checkingAccountList = session.createQuery("from CheckingAccount").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return checkingAccountList;
    }


    //get Checking Account by ID
    public CheckingAccount getCheckingAccountByID(long id) {
        Transaction transaction = null;
        CheckingAccount checkingAccount = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get Checking Account object
            checkingAccount = session.get(CheckingAccount.class, id);

//            admin = session.load(Admin.class, id);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return checkingAccount;
    }

    //Update Checking Account
    public void updateCheckingAccount(CheckingAccount checkingAccount) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save Checking Account object
            session.saveOrUpdate(checkingAccount);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    //Detele Checking Account
    public CheckingAccount deleteCheckingAccount(String owner) {
        Transaction transaction = null;
        CheckingAccount checkingAccount = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get Checking Account object
            checkingAccount = session.createQuery("from CheckingAccount where owner = :owner",CheckingAccount.class).setParameter("owner", owner).uniqueResult();
            session.delete(checkingAccount);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return checkingAccount;
    }
}
