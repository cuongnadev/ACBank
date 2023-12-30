package com.example.javafx.View;

import com.example.javafx.Controller.Admin.AdminController;
import com.example.javafx.Controller.Admin.ClientsController;
import com.example.javafx.Controller.Admin.ReceiptController;
import com.example.javafx.Controller.Admin.SignUpListController;
import com.example.javafx.Controller.Client.*;
import com.example.javafx.Controller.LoginController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    private AccountType loginAccountType;
    //Client View
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane transactionsView;
    private AnchorPane accountsView;
    private LoginController loginController;
    private ClientsController clientsController;
    private DashboardController dashboardController;
    private AccountsController accountsController;
    private TransactionsController transactionsController;
    private ReceiptController receiptController;
    private SignUpController signUpController;
    private SignUpListController signUpListController;
    //getter & setter

    public LoginController getLoginController(){
        return this.loginController;
    }
    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }
    public ClientsController getClientsController(){return this.clientsController; }
    public void setClientsController (ClientsController clientsController){this.clientsController = clientsController; }
    public DashboardController getDashboardController (){return this.dashboardController;}
    public void setDashboardController(DashboardController dashboardController){this.dashboardController = dashboardController;}
    public AccountsController getAccountsController () {
        return this.accountsController;
    }
    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }
    public TransactionsController getTransactionsController() {
        return this.transactionsController;
    }
    public void setTransactionsController(TransactionsController transactionsController) {
        this.transactionsController = transactionsController;
    }

    public ReceiptController getReceiptController() {
        return receiptController;
    }

    public void setReceiptController(ReceiptController receiptController) {
        this.receiptController = receiptController;
    }

    public SignUpListController getSignUpListController(){
        return this.signUpListController ;
    }
    public void setSignUpListController (SignUpListController signUpListController){
        this.signUpListController = signUpListController;
    }

    public SignUpController getSignUpController() {
        return signUpController;
    }

    public void setSignUpController(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    // Admin View
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane createClientView;
    private AnchorPane clientsView;
    private AnchorPane depositView;
    private AnchorPane signUpListView;



    public ViewFactory(){
        this.loginAccountType = AccountType.CLIENT;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType(){
        return loginAccountType;
    }
    public void setLoginAccountType(AccountType loginAccountType){
        this.loginAccountType = loginAccountType;
    }


    /*
    * Client Views Section
    * */
    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem (){
        return clientSelectedMenuItem;
    }
    public AnchorPane getDashboardView(){
        try {
            dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Client/Dashboard.fxml")).load();
        } catch (Exception e){
            e.printStackTrace();
        }
        return dashboardView;
    }
    public AnchorPane getTransactionsView(){
        if (transactionsView == null){
            try {
                transactionsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Transactions.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return transactionsView;
    }
    public AnchorPane getAccountsView(){
        if (accountsView == null){
            try {
                accountsView = new FXMLLoader(getClass().getResource("/Fxml/Client/Accounts.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return accountsView;
    }
    public void showClientWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }
    public void showSignUpWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/SignUp.fxml"));
        SignUpController signUpController = new SignUpController();
        loader.setController(signUpController);
        createStage(loader);
    }



    /*
    * Admin Views Section
    * */
    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem(){
        return adminSelectedMenuItem;
    }

    public AnchorPane getSignUpListView(){
        if (signUpListView == null){
            try {
                signUpListView = new FXMLLoader(getClass().getResource("/Fxml/Admin/SignUpList.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return signUpListView;
    }

    public AnchorPane getReceiptView(){
        if (createClientView == null){
            try {
                createClientView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Receipts.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return createClientView;
    }

    public AnchorPane getClientsView() {
        if (clientsView == null){
            try {
                clientsView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Clients.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return clientsView;
    }

    public AnchorPane getDepositView() {
        if (depositView == null){
            try {
                depositView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Deposit.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return depositView;
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }




    // Show Window
    public void showLoginWindow (){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("ACBank");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}
