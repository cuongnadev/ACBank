package com.example.javafx.Dao;

import com.example.javafx.Models.Admin;
import com.example.javafx.Models.Clients;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class AdminDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save Admin
    public void saveAdmin(Admin admin) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save admin object
            session.save(admin);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }


    //get All Admin
    public List<Admin> getAllAdmins() {
        Transaction transaction = null;
        List<Admin> adminList = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get admin object
            adminList = session.createQuery("from Admin").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return adminList;
    }


    //get Admin by ID
    public Admin getAdminByID(long id) {
        Transaction transaction = null;
        Admin admin = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get admin object
            admin = session.get(Admin.class, id);

//            admin = session.load(Admin.class, id);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return admin;
    }

    //Update Admin
    public void updateAdmin(Admin admin) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save admin object
            session.saveOrUpdate(admin);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    //Detele Admin
    public Admin deleteAdmin(long id) {
        Transaction transaction = null;
        Admin admin = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get admin object
            admin = session.get(Admin.class, id);
            session.delete(admin);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return admin;
    }
}
