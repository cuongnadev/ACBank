package com.example.javafx.Dao;

import com.example.javafx.Models.Admin;
import com.example.javafx.Models.CheckingAccount;
import com.example.javafx.Models.Clients;
import com.example.javafx.Models.SavingAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class SavingAccountDao {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    //save Saving Account
    public void saveSavingAccount(SavingAccount savingAccount) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save Saving Account object
            session.save(savingAccount);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(transaction != null) {
                transaction.rollback();
            }

        }
    }


    //get All Saving Account
    public List<SavingAccount> getAllSavingAccounts() {
        Transaction transaction = null;
        List<SavingAccount> savingAccountList = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get Saving Account object
            savingAccountList = session.createQuery("from SavingAccount").list();


            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return savingAccountList;
    }


    //get Saving Account by ID
    public SavingAccount getSavingAccountByID(long id) {
        Transaction transaction = null;
        SavingAccount savingAccount = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get saving Account object
            savingAccount = session.get(SavingAccount.class, id);

//            admin = session.load(Admin.class, id);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return savingAccount;
    }

    //Update Saving Account
    public void updateSavingAccount(SavingAccount savingAccount) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //save saving Account object
            session.saveOrUpdate(savingAccount);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
    }

    //Detele Saving Account
    public SavingAccount deleteSavingAccount(String accountNum) {
        Transaction transaction = null;
        SavingAccount savingAccount = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            //get Checking Account object
            savingAccount = session.createQuery("from SavingAccount where accountNumber = :accountNumber",SavingAccount.class).setParameter("accountNumber", accountNum).uniqueResult();
            session.delete(savingAccount);

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
        }
        return savingAccount;
    }

    // Get Saving Account with maximum balance for a specific owner
    public SavingAccount getSavingAccountsDataBalanceMax(String owner) {
        Transaction transaction = null;
        SavingAccount maxBalanceAccount = null;
        try (Session session = sessionFactory.openSession()) {
            // start the transaction
            transaction = session.beginTransaction();

            // get the list of Saving Accounts with the given owner
            List<SavingAccount> savingAccountList = session.createQuery("from SavingAccount where owner = :owner", SavingAccount.class)
                    .setParameter("owner", owner)
                    .list();

            // find the account with the maximum balance
            if (savingAccountList != null && !savingAccountList.isEmpty()) {
                maxBalanceAccount = savingAccountList.stream()
                        .max((a1, a2) -> Double.compare(a1.getBalance(), a2.getBalance()))
                        .orElse(null);
            }

            //commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return maxBalanceAccount;
    }
}
