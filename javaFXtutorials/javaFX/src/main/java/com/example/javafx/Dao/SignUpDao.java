package com.example.javafx.Dao;

import com.example.javafx.Models.Admin;
import com.example.javafx.Models.Receipt;
import com.example.javafx.Models.SignUp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class SignUpDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save SignUp
    public void saveSignUp(SignUp signUp) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save signUp object
            session.save(signUp);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }


    //get All signup
    public List<SignUp> getAllsignUps() {
        Transaction transaction = null;
        List<SignUp> signUpList = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get signup object
            signUpList = session.createQuery("from SignUp").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return signUpList;
    }


    //get signup by ID
    public SignUp getSignUpByID(long id) {
        Transaction transaction = null;
        SignUp signUp = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get receipt object
            signUp = session.get(SignUp.class, id);

//            admin = session.load(Admin.class, id);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return signUp;
    }

    //Update signUp
    public void updateSignUp(SignUp signUp) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save signUp object
            session.saveOrUpdate(signUp);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    //Detele signUp
    public SignUp deleteSignUp(String pAddress) {
        Transaction transaction = null;
        SignUp signUp = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get signUp object
            signUp = session.createQuery("from SignUp where pAddress = :pAddress", SignUp.class).setParameter("pAddress", pAddress).uniqueResult();
            session.delete(signUp);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return signUp;
    }
}
