package com.example.javafx.Dao;

import com.example.javafx.Models.Admin;
import com.example.javafx.Models.Clients;
import com.example.javafx.Models.ForgotPass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ForgotpassDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save Forgotpass
    public void saveForgotpass(ForgotPass forgotPass) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save forgot object
            session.save(forgotPass);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }

    //get All Forgot
    public List<ForgotPass> getAllForgots() {
        Transaction transaction = null;
        List<ForgotPass> forgotPassList = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get student object
            forgotPassList = session.createQuery("from ForgotPass ").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return forgotPassList;
    }


    //Detele Forgotpass
    public ForgotPass deleteForgotpass(String pAddress) {
        Transaction transaction = null;
        ForgotPass forgotPass = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get Forgotpass object
            forgotPass = session.createQuery("from ForgotPass where pAddress = :pAddress", ForgotPass.class).setParameter("pAddress", pAddress).uniqueResult();
            session.delete(forgotPass);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return forgotPass;
    }
}
