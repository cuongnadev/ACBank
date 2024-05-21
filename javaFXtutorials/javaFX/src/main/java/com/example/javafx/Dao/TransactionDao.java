package com.example.javafx.Dao;

import com.example.javafx.Models.Admin;
import com.example.javafx.Models.Clients;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class TransactionDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save Transaction
    public void saveTransaction(com.example.javafx.Models.Transaction myTransaction) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save admin object
            session.save(myTransaction);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }


    //get All myTransaction
    public List<com.example.javafx.Models.Transaction> getAllTransactions() {
        Transaction transaction = null;
        List<com.example.javafx.Models.Transaction> transactionList = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get admin object
            transactionList = session.createQuery("from Transaction").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return transactionList;
    }


    //get myTransaction by ID
    public com.example.javafx.Models.Transaction getTransactionByID(long id) {
        Transaction transaction = null;
        com.example.javafx.Models.Transaction myTransaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get admin object
            myTransaction = session.get(com.example.javafx.Models.Transaction.class, id);

//            admin = session.load(Admin.class, id);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return myTransaction;
    }

    //Update myTransaction
    public void updateTransaction(Transaction myTransaction) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save Transaction object
            session.saveOrUpdate(myTransaction);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    //Detele Transaction
    public com.example.javafx.Models.Transaction deleteTransaction(String pAddress) {
        Transaction transaction = null;
        com.example.javafx.Models.Transaction myTransaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get admin object
            myTransaction = session.createQuery("from Transaction where sender = :pAddress or receiver = :pAddress", com.example.javafx.Models.Transaction.class).setParameter("pAddress", pAddress).uniqueResult();
            session.delete(myTransaction);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return myTransaction;
    }
}
