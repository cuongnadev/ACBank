package com.example.javafx;

import com.example.javafx.Dao.AdminDao;
import com.example.javafx.Dao.ClientsDao;
import com.example.javafx.Models.Admin;
import com.example.javafx.Models.Clients;

import java.util.List;

public class main {
    public static void main(String[] args) {
//        ClientsDao clientsDao = new ClientsDao();
        AdminDao adminDao = new AdminDao();

        //test save
//        Clients client = new Clients("Nguyen", "Hung", "NgHung1405", "123456", "2024-05-15");
//        clientsDao.saveClient(client);
//
//
//        //test getStudenByID
//        Clients client2 = clientsDao.getClientByID(client.getId());
        Admin admin = adminDao.getAdminByID(1);
        System.out.println(admin.getUserName());

    }
}
