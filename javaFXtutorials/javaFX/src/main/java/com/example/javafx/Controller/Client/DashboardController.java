package com.example.javafx.Controller.Client;

import com.example.javafx.Models.*;
import com.example.javafx.View.TransactionCellFactory;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import com.itextpdf.layout.properties.TextAlignment;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;


import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_today;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label saving_bal;
    public Label saving_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView<Transaction> transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea massage_fld;
    public Button send_money_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataLabel();
        refreshDataTransaction();
        List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
        transaction_listview.getItems().addAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory());
        send_money_btn.setOnAction(event -> onSendMoney());
        Model.getInstance().getViewFactory().setDashboardController(this);
    }

    public void setDataLabel(){
        int Id = Model.getInstance().getClients().getId();
        String pAddress = Model.getInstance().getClients().getPayeeAddress();
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        List<Transaction> transactionList = Model.getInstance().getDaoDriver().getTransactionDao().getAllTransactions();
        double income = 0;
        double expense = 0;
        for (Clients client : clientsList) {
            if (client.getId() == Id){
                String LName = client.getLastName();
                user_name.setText("Hi, "+ LName);
                break;
            }
        }
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        login_today.setText("Today, " + formattedDate);
        for (CheckingAccount checkingAccount : checkingAccountList){
            if (pAddress.equals(checkingAccount.getOwner())){
                checking_acc_num.setText(checkingAccount.getAccountNumber());
                checking_bal.setText(String.valueOf(checkingAccount.getBalance()));

            }
        }
        SavingAccount savingAccount = Model.getInstance().getDaoDriver().getSavingAccountDao().getSavingAccountsDataBalanceMax(pAddress);
        saving_acc_num.setText(savingAccount.getAccountNumber());
        saving_bal.setText(String.valueOf(savingAccount.getBalance()));

        for (Transaction transaction : transactionList){
            if (pAddress.equals(transaction.getSender())){
                expense += transaction.getAmount();
            } else if (pAddress.equals(transaction.getReceiver())){
                income += transaction.getAmount();
            }
        }
        income_lbl.setText(String.valueOf(income));
        expense_lbl.setText(String.valueOf(expense));
    }

    //Send Money
    private void onSendMoney() {
        // Step 1: Lấy dữ liệu từ field
        String pAddress_receiver = payee_fld.getText().trim();
        double amount;
        try {
            amount = Double.parseDouble(amount_fld.getText().trim());
        } catch (NumberFormatException e) {
            showAlertErorr("Invalid amount. Please enter a valid number.");
            return;
        }
        String message = massage_fld.getText().trim();

        // Step 2: Xác thực dữ liệu
        if (pAddress_receiver.isEmpty() || amount <= 0) {
            showAlertErorr("Please enter valid Payee or Amount.");
            return;
        }

        // Step 3:Cập nhật số dư tài khoản và thêm transaction mới
        String pAddress_sender = Model.getInstance().getClients().getPayeeAddress();

        // Truy xuất thông tin tài khoản của người gửi
        List<CheckingAccount> checkingAccountList = Model.getInstance().getDaoDriver().getCheckingAccountDao().getAllCheckingAccounts();
        for (CheckingAccount checkingAccount : checkingAccountList) {
            if (pAddress_sender.equals(checkingAccount.getOwner())) {
                double senderBalance = checkingAccount.getBalance();
                if (senderBalance < amount) {
                    showAlertErorr("Insufficient funds. Please check your balance.");
                    return;
                }
                // Truy xuất thông tin tài khoản của người nhận thanh toán
                for (CheckingAccount checkingAccount1 : checkingAccountList) {
                    if (pAddress_receiver.equals(checkingAccount1.getOwner())) {
                        // Cập nhật số dư của người gửi
                        checkingAccount.setBalance(senderBalance - amount);
                        Model.getInstance().getDaoDriver().getCheckingAccountDao().updateCheckingAccount(checkingAccount);

                        // Cập nhật số dư người nhận
                        double payeeBalance = checkingAccount1.getBalance();
                        checkingAccount1.setBalance(payeeBalance + amount);
                        Model.getInstance().getDaoDriver().getCheckingAccountDao().updateCheckingAccount(checkingAccount1);

                        //Thêm giao dịch mới
                        Transaction newTransaction = new Transaction(
                                pAddress_sender,
                                pAddress_receiver,
                                amount,
                                LocalDate.now().toString(),
                                message);
                        Model.getInstance().getDaoDriver().getTransactionDao().saveTransaction(newTransaction);

                        // Làm mới transaction listview
                        List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
                        transaction_listview.getItems().setAll(transactions);

                        String IDBienLai = RanDomIDBienLai(pAddress_sender , pAddress_receiver);

                        Receipt receipt = new Receipt(IDBienLai, pAddress_sender, pAddress_receiver, checkingAccount.getAccountNumber(), checkingAccount1.getAccountNumber(), amount, LocalDate.now().toString() , message) ;

                        inBienLai( IDBienLai, pAddress_sender , pAddress_receiver , checkingAccount.getAccountNumber() , checkingAccount1.getAccountNumber() , amount , LocalDate.now().toString() , message );
                        showAlertSuccessful("Successful money transfer");
                        setDataLabel();
                        break;
                    }else {
                        showAlertErorr("Please enter valid PayeeAddress.");
                        return;
                    }
                }
                break;
            }
        }
    }



    private List<Transaction> getTransactionOfSQLiteLimit(int limit) {
        transaction_listview.getItems().clear();
        String pAdress = Model.getInstance().getClients().getPayeeAddress();
        List<Transaction> transactionList = Model.getInstance().getDaoDriver().getTransactionDao().getAllTransactions();
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (pAdress.equals(transaction.getSender())
                || pAdress.equals(transaction.getReceiver())){
                Transaction newtTransaction = new Transaction(
                        transaction.getSender(),
                        transaction.getReceiver(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getMessage());
                transactions.add(newtTransaction);
            }
        }
        // Sắp xếp danh sách theo ngày giảm dần
        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        // Giới hạn số lượng transactions
        return transactions.subList(0, Math.min(limit, transactions.size()));
    }

    public void refreshDataTransaction(){
        List<Transaction> transactions = getTransactionOfSQLiteLimit(4);
        transaction_listview.getItems().setAll(transactions);
        transaction_listview.setCellFactory(listView -> new TransactionCellFactory());
    }
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

    private void showAlertErorr(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private String RanDomIDBienLai(String sender , String receiver){
        Random random = new Random();
        int ranDomNumber = random.nextInt(1000);
        return "BienLai_" + Character.toUpperCase(sender.charAt(1)) + Character.toUpperCase(receiver.charAt(1)) + ranDomNumber;
    }

    private void showAlertSuccessful(String successfulMoneyTransfer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(successfulMoneyTransfer);
        alert.showAndWait();
    }
}
