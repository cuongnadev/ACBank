package com.example.javafx.Controller.Admin;

import com.example.javafx.Models.Clients;
import com.example.javafx.Models.ForgotPass;
import com.example.javafx.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;


import java.net.URL;
import java.sql.ResultSet;
import java.util.*;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassCellController implements Initializable {
    public Label pAddress_lbl;
    public Label date_lbl;
    public Button no_btn;
    public Button reset_btn;
    public Label email_lbl;
    private final ForgotPass forgotPass;
    public ForgotPassCellController (ForgotPass forgotPass){
        this.forgotPass = forgotPass;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataLabel();
        reset_btn.setOnAction(event -> onReset());
        no_btn.setOnAction(event -> onNO());
    }

    private void setDataLabel() {
        pAddress_lbl.setText(forgotPass.getpAddress());
        date_lbl.setText(forgotPass.getDate());
        email_lbl.setText(forgotPass.getEmail());
    }
    private void onReset() {
        int passRandom = RanDomNumber();
        String newPass = Model.HashPassword(String.valueOf(passRandom));
        sendmail(email_lbl.getText() , String.valueOf(passRandom) , pAddress_lbl.getText());
        List<Clients> clientsList = Model.getInstance().getDaoDriver().getClientsDao().getAllClients();
        List<ForgotPass> forgotPassList = Model.getInstance().getDaoDriver().getForgotpassDao().getAllForgots();
        for(Clients client : clientsList) {
            if(client.getPayeeAddress().equals(pAddress_lbl.getText())) {
                client.setPassword(newPass);
                Model.getInstance().getDaoDriver().getClientsDao().updateClient(client);
            }
        }
        for(ForgotPass forgotPass : forgotPassList) {
            if(forgotPass.getpAddress().equals(pAddress_lbl.getText())) {
                Model.getInstance().getDaoDriver().getForgotpassDao().deleteForgotpass(pAddress_lbl.getText());
            }
        }
        Model.getInstance().getViewFactory().getSignUpListController().refreshClientsListView();
        showAlertSuccessful("Reset Password of " + pAddress_lbl.getText() + " Successfull");
    }
    private void onNO() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("You definitely don't want to reset password for "+pAddress_lbl.getText()+"?");

        Optional<ButtonType> option = alert.showAndWait();
        if(option.get().equals(ButtonType.OK)) {
            Model.getInstance().getDaoDriver().getForgotpassDao().deleteForgotpass(pAddress_lbl.getText());
            Model.getInstance().getViewFactory().getSignUpListController().refreshClientsListView();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Deleted!");
            alert.showAndWait();
        }
    }
    public void showAlertSuccessful(String message ){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public int RanDomNumber(){
        Random random = new Random();
        return 100000 + random.nextInt(9000);
    }
    public void sendmail(String mail , String newPassword , String pAddress) {

        final String username = "2005anhcuong@gmail.com";
        final String password = "fqgpzunitosmkurm";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("2005anhcuong@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("Password Reset");
            message.setText("Hello, " + pAddress + "\nYour new password is: " + newPassword);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
