package com.example.javafx.Controller.Client;

import com.example.javafx.Controller.LoginController;
import com.example.javafx.Models.DatabaseDriver;
import com.example.javafx.Models.Model;
import com.example.javafx.Models.Transaction;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.sql.SQLException;

public class TransactionCellController implements Initializable {
    public FontIcon in_icon;
    public FontIcon out_icon;
    public Label trans_date_lbl;
    public Label sender_lbl;
    public Label receiver_lbl;
    public Label amount_lbl;
    public FontIcon message_icon;
    public Button print_btn;

    private final Transaction transaction;

    public TransactionCellController(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDataToLabels();
        message_icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showMessage();
            }
        });
        in_out_icon();
        print_btn.setOnAction(event -> onPrint());
    }

    public void onPrint() {
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().getReceiptData();
        try {
            while (resultSet.next()){
                if (sender_lbl.getText().trim().equals(resultSet.getString("Sender")) &&
                        receiver_lbl.getText().trim().equals(resultSet.getString("Receiver")) &&
                        trans_date_lbl.getText().equals(resultSet.getString("Date"))&&
                        amount_lbl.getText().equals(String.valueOf(resultSet.getDouble("Amount")))){
                    String IDBienLai = null;
                    String sender = null;
                    String receiver = null;
                    String numberSender = null;
                    String numberReceiver = null;
                    double amount = 0;
                    String date = null;
                    String message = null;
                    try {
                        IDBienLai = resultSet.getString("IDBienLai");
                        sender = resultSet.getString("Sender");
                        receiver = resultSet.getString("Receiver");
                        numberSender = resultSet.getString("NumberSender");
                        numberReceiver = resultSet.getString("NumberReceiver");
                        amount = resultSet.getDouble("Amount");
                        date = resultSet.getString("Date");
                        message = resultSet.getString("Message");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    //in file pdf
                    String path = "D:\\BaiTapLon\\javaFXtutorials\\ClientBienLai\\" + IDBienLai + ".pdf";

                    try {
                        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(path)));
                        Document document = new Document(pdfDocument);

                        Paragraph para0 = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------")
                                .setTextAlignment(TextAlignment.CENTER);
                        Paragraph para1 = new Paragraph("               NGAN HANG DOI MOI VA SANG TAO               ")
                                .setFontColor(new DeviceRgb(0, 0, 0))
                                .setBold()
                                .setFontSize(20)
                                .setTextAlignment(TextAlignment.CENTER);

                        Paragraph para15 = new Paragraph("                           ACBANK                                ")
                                .setFontColor(new DeviceRgb(30, 130, 70))
                                .setBold()
                                .setFontSize(26)
                                .setTextAlignment(TextAlignment.CENTER);
                        Paragraph para2 = new Paragraph("                    Money Transfer Receipt                       ")
                                .setFontColor(new DeviceRgb(0, 0, 0))
                                .setItalic()
                                .setFontSize(20)
                                .setTextAlignment(TextAlignment.CENTER);
                        Paragraph para3 = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------")
                                .setTextAlignment(TextAlignment.CENTER);
                        Paragraph para35 = new Paragraph("ID Receipt: " + IDBienLai + "                                        ")
                                .setFontSize(15);
                        Paragraph para4 = new Paragraph("Sender: " + sender + "                                               ")
                                .setFontSize(15);
                        Paragraph para45 = new Paragraph("AccountNumber Sender: " + numberSender + "                           ")
                                .setFontSize(15);
                        Paragraph para5 = new Paragraph("                    ***** Giao Dich *****                        ")
                                .setFontColor(new DeviceRgb(0, 0, 0))
                                .setItalic()
                                .setFontSize(18)
                                .setTextAlignment(TextAlignment.CENTER);
                        Paragraph para55 = new Paragraph("Receiver: " + receiver + "                                           ")
                                .setFontSize(15);
                        Paragraph para6 = new Paragraph("AccountNumber Receiver: " + numberReceiver + "                       ")
                                .setFontSize(15);
                        Paragraph para65 = new Paragraph("Amount: " + amount + "$                                               ")
                                .setBold()
                                .setFontSize(18);
                        Paragraph para7 = new Paragraph("Date: " + date + "                                                   ")
                                .setFontSize(15)
                                .setItalic();
                        Paragraph para75 = new Paragraph("                     ****" + message + "****                         ")
                                .setFontColor(new DeviceRgb(0, 0, 0))
                                .setItalic()
                                .setFontSize(18)
                                .setTextAlignment(TextAlignment.CENTER);
                        Paragraph para8 = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------")
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
                    showAlert("Print Successfull");
                    break;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    
    

    private void setDataToLabels() {

        trans_date_lbl.setText(transaction.dateProperty().get());
        sender_lbl.setText(transaction.senderProperty().get());
        receiver_lbl.setText(transaction.receiverProperty().get());
        amount_lbl.setText(String.valueOf(transaction.amountProperty().get()));
    }
    private void showMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transaction Message");
        alert.setHeaderText(null);
        alert.setContentText(transaction.messageProperty().get());
        alert.showAndWait();
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void in_out_icon(){
        LoginController loginController = Model.getInstance().getViewFactory().getLoginController();
        if (sender_lbl.getText().equals(loginController.payee_address_fid.getText())){
            in_icon.setIconColor(Color.RED);
            out_icon.setIconColor(Color.GREY);
        }
        if (receiver_lbl.getText().equals(loginController.payee_address_fid.getText())){
            out_icon.setIconColor(Color.GREEN);
            in_icon.setIconColor(Color.GREY);
        }
    }
}

