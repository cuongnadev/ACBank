package com.example.javafx.Dao;

import com.example.javafx.Models.Clients;
import com.example.javafx.Models.SignUp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class ClientsDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save Client
    public void saveClient(Clients clients) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save student object
            session.save(clients);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }


    //get All Client
    public List<Clients> getAllClients() {
        Transaction transaction = null;
        List<Clients> students = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get student object
            students = session.createQuery("from Clients").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return students;
    }


    //get Student by ID
    public Clients getClientByID(long id) {
        Transaction transaction = null;
        Clients student = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get student object
            student = session.get(Clients.class, id);

//            student = session.load(Student.class, id);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return student;
    }

    //get client by pAddress
    public Clients getClientByPayeeAddress(String pAddress) {
        Transaction transaction = null;
        Clients student = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get student object
//            student = session.get(Clients.class, pAddress);

            //
            student = session.createQuery("from Clients where payeeAddress = :pAddress", Clients.class).setParameter("pAddress", pAddress).uniqueResult();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return student;
    }

    //Update Client
    public void updateClient(Clients clients) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save student object
            session.saveOrUpdate(clients);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    //Detele Client
    public Clients deleteClient(long id) {
        Transaction transaction = null;
        Clients clients = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get student object
            clients = session.get(Clients.class, id);
            session.delete(clients);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return clients;
    }
}

