package com.example.javafx.Dao;

import com.example.javafx.Models.Admin;
import com.example.javafx.Models.Receipt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ReceiptDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save Receipt
    public void saveReceipt(Receipt receipt) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save receipt object
            session.save(receipt);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }


    //get All receipt
    public List<Receipt> getAllReceipts() {
        Transaction transaction = null;
        List<Receipt> receiptList = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get receipt object
            receiptList = session.createQuery("from Receipt ").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return receiptList;
    }


    //get receipt by ID
    public Receipt getReceiptByID(long id) {
        Transaction transaction = null;
        Receipt receipt = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get receipt object
            receipt = session.get(Receipt.class, id);

//            admin = session.load(Admin.class, id);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return receipt;
    }

    //Update Receipt
    public void updateReceipt(Receipt receipt) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save receipt object
            session.saveOrUpdate(receipt);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    //Detele receipt
    public Receipt deleteReceipt(String Id) {
        Transaction transaction = null;
        Receipt receipt = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get receipt object
            receipt = session.createQuery("from Receipt where IDReceipt = :IDReceipt", Receipt.class).setParameter("IDReceipt", Id).uniqueResult();
            session.delete(receipt);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return receipt;
    }
}
