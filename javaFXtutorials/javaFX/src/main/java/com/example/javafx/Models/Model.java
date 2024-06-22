package com.example.javafx.Models;


import com.example.javafx.Client.ClientHandler;
import com.example.javafx.Dao.DaoDriver;
import com.example.javafx.Server.BankServer;
import com.example.javafx.View.ViewFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DaoDriver daoDriver;
    private BankServer server;
    private final ThreadPoolExecutor clientExecutor;
    private final Map<String, ClientHandler> clientHandlers = new HashMap<>();


    // Client Data Section
    private final Clients currentClient;
    private final ObservableList<Clients> onlineClientList;
    private Boolean clientLoginSuccessFlag;


    //Admin Data Section
    private final Admin admin;
    private Boolean adminLoginSuccessFlag;

    private Model () throws IOException {
        this.daoDriver = new DaoDriver();
        this.viewFactory = new ViewFactory();

        //Client Data Section
        this.clientLoginSuccessFlag = false;
        this.currentClient = new Clients("", "", "", "","", "");
        this.onlineClientList = FXCollections.observableArrayList();

        //Admin Data Section
        this.adminLoginSuccessFlag = false ;
        this.admin = new Admin("" ,"");


        clientExecutor = new ThreadPoolExecutor(10, 50, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(8));

    }

    public static synchronized Model getInstance(){
        if (model  == null){
            try {
                model = new Model();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    public void startServer() {
        if (server == null) {
            try {
                server = new BankServer(44105);
                new Thread(() -> server.start()).start();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }


    public BankServer getServer() {
        return server;
    }

    public ThreadPoolExecutor getClientExecutor() {
        return clientExecutor;
    }

    public Map<String, ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public ViewFactory getViewFactory(){
        return viewFactory;
    }
    public DaoDriver getDaoDriver() {
        return daoDriver;
    }

    /*
    * Client Method Section
    * */
    public boolean getClientLoginSuccessFlag(){
        return this.clientLoginSuccessFlag;
    }
    public void setClientLoginSuccessFlag (boolean flag){
        this.clientLoginSuccessFlag = flag;
    }
    public void evaluateClientCred(String pAddress){
        List<Clients> clientsList = this.getDaoDriver().getClientsDao().getAllClients();
        String nameAdmin = Model.getInstance().getAdmin().getUserName();
        try {
            for (Clients client : clientsList) {
                if(client.getPayeeAddress().equals(pAddress) && client.getAdminName().equals(nameAdmin)) {
                    this.currentClient.setId(client.getId());
                    this.currentClient.setPayeeAddress(client.getPayeeAddress());
                    this.currentClient.setPassword(client.getPassword());
                    this.currentClient.setFirstName(client.getFirstName());
                    this.currentClient.setLastName(client.getLastName());
                    this.currentClient.setDateCreated(client.getDateCreated());
                    this.currentClient.setAdminName(client.getAdminName());
                    this.setClientLoginSuccessFlag(true);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // Client Request
    public ObservableList<Clients> getOnlineClientList() {
        return onlineClientList;
    }

    public Clients getCurrentClient(){
        return currentClient;
    }

    public boolean transferMoney(String senderId, String receiverId, double amount, String messageContent) {
        try {
            // Retrieve sender and receiver accounts
            CheckingAccount senderAccount = daoDriver.getCheckingAccountDao().getCheckingAccountByID(Integer.parseInt(senderId));
            CheckingAccount receiverAccount = daoDriver.getCheckingAccountDao().getCheckingAccountByID(Integer.parseInt(receiverId));

            if (senderAccount == null || receiverAccount == null) {
                return false; // Invalid sender or receiver
            }

            // Check if sender has enough balance
            if (senderAccount.getBalance() < amount) {
                return false; // Insufficient balance
            }

            // Perform the transfer
            senderAccount.setBalance(senderAccount.getBalance() - amount);
            receiverAccount.setBalance(receiverAccount.getBalance() + amount);

            // Update accounts in the database
            daoDriver.getCheckingAccountDao().updateCheckingAccount(senderAccount);
            daoDriver.getCheckingAccountDao().updateCheckingAccount(receiverAccount);

            // Create a new transaction record
            Transaction transaction = new Transaction(
                    senderAccount.getOwner(), // Sender's address
                    receiverAccount.getOwner(), // Receiver's address
                    amount,
                    java.time.LocalDate.now().toString(),
                    messageContent
            );

            daoDriver.getTransactionDao().saveTransaction(transaction);

            String adminName = getAdmin().getUserName();
            String IDBienLai = RanDomIDBienLai(senderAccount.getOwner() , receiverAccount.getOwner());

            Receipt receipt = new Receipt(IDBienLai, senderAccount.getOwner() , receiverAccount.getOwner(), senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), amount, LocalDate.now().toString() , messageContent, adminName) ;
            getDaoDriver().getReceiptDao().saveReceipt(receipt);

            inBienLai( IDBienLai, senderAccount.getOwner() , receiverAccount.getOwner(), senderAccount.getAccountNumber(), receiverAccount.getAccountNumber() , amount , LocalDate.now().toString() , messageContent );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /*
    * Admin Method Section
    * */
    public Boolean getAdminLoginSuccessFlag() {
        return adminLoginSuccessFlag;
    }
    public void setAdminLoginSuccessFlag(Boolean Flag) {
        this.adminLoginSuccessFlag = Flag;
    }
    public Admin getAdmin(){
        return admin;
    }
    public void evaluateAdminCred(String username , String password){
        List<Admin> adminList = this.getDaoDriver().getAdminDao().getAllAdmins();
        try {
            for(Admin admin1 : adminList) {
                if(admin1.getUserName().equals(username) && admin1.getPassword().equals(password)) {
                    this.getAdmin().setUserName(username);
                    this.getAdmin().setPassword(password);
                    this.adminLoginSuccessFlag = true;
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    // hash Password
    public static String HashPassword(String password){
        try {
            MessageDigest pass = MessageDigest.getInstance("MD5");
            pass.update(password.getBytes());
            byte[] hashedBytes = pass.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //
    private String RanDomIDBienLai(String sender , String receiver){
        Random random = new Random();
        int ranDomNumber = random.nextInt(1000);
        return "BienLai_" + Character.toUpperCase(sender.charAt(1)) + Character.toUpperCase(receiver.charAt(1)) + ranDomNumber;
    }

    //
    public void inBienLai(String IDBienLai , String sender , String receiver , String numberSender , String numberReceiver , double amount , String date , String message ){
        //in file pdf
        String path = "D:\\BaiTapLon\\javaFXtutorials\\BienLai\\" + IDBienLai + ".pdf";

        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(path)));
            Document document = new Document(pdfDocument);

            Paragraph para0 =  new Paragraph("----------------------------------------------------------------------------------------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph para1 =  new Paragraph("               NGAN HANG DOI MOI VA SANG TAO               ")
                    .setFontColor(new DeviceRgb(0,0,0))
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);

            Paragraph para15 = new Paragraph("                           ACBANK                                ")
                    .setFontColor(new DeviceRgb(30,130,70))
                    .setBold()
                    .setFontSize(26)
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph para2 =  new Paragraph("                    Money Transfer Receipt                       ")
                    .setFontColor(new DeviceRgb(0,0,0))
                    .setItalic()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph para3 =  new Paragraph("----------------------------------------------------------------------------------------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph para35 = new Paragraph("ID Receipt: "+IDBienLai+"                                        ")
                    .setFontSize(15);
            Paragraph para4 =  new Paragraph("Sender: "+sender+"                                               ")
                    .setFontSize(15);
            Paragraph para45 = new Paragraph("AccountNumber Sender: "+numberSender+"                           ")
                    .setFontSize(15);
            Paragraph para5 =  new Paragraph("                    ***** Giao Dich *****                        ")
                    .setFontColor(new DeviceRgb(0,0,0))
                    .setItalic()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph para55 = new Paragraph("Receiver: "+receiver+"                                           ")
                    .setFontSize(15);
            Paragraph para6 =  new Paragraph("AccountNumber Receiver: "+numberReceiver+"                       ")
                    .setFontSize(15);
            Paragraph para65 = new Paragraph("Amount: "+amount+"$                                               ")
                    .setBold()
                    .setFontSize(18);
            Paragraph para7 =  new Paragraph("Date: "+date+"                                                   ")
                    .setFontSize(15)
                    .setItalic();
            Paragraph para75 = new Paragraph("                     **** "+message+" ****                         ")
                    .setFontColor(new DeviceRgb(0,0,0))
                    .setItalic()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER);
            Paragraph para8 =  new Paragraph("----------------------------------------------------------------------------------------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(para0);
            document.add(para1);
            document.add(para15);
            document.add(para2);
            document.add(para3);
            document.add(para35);
            document.add(para4);
            document.add(para45);
            document.add(para5);
            document.add(para55);
            document.add(para6);
            document.add(para65);
            document.add(para7);
            document.add(para75);
            document.add(para8);

            document.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
