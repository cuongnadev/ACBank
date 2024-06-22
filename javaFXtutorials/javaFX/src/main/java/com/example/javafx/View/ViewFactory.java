package com.example.javafx.View;

import com.example.javafx.Client.ClientSession;
import com.example.javafx.Controller.Admin.*;
import com.example.javafx.Controller.Client.*;
import com.example.javafx.Controller.LoginController;
import com.example.javafx.Models.Model;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ViewFactory {
    public ViewFactory(){
        this.loginAccountType = AccountType.ADMIN;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    private AccountType loginAccountType;

    //Client View
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private final Map<String, ClientSession> clientSessions = new HashMap<>();
    private AnchorPane dashboardView;
    private AnchorPane transactionsView;
    private AnchorPane accountsView;
    private AnchorPane profileView;
    private LoginController loginController;
    private ClientsController clientsController;
    private DashboardController dashboardController;
    private AccountsController accountsController;
    private ProfileController profileController;
    private TransactionsController transactionsController;
    private TransactionCellController transactionCellController;
    private ReceiptController receiptController;
    private SignUpController signUpController;
    private SignUpListController signUpListController;
    private DepositController depositController;
    private ClientMenuController clientMenuController;

    //getter & setter

    //Login
    public LoginController getLoginController(){
        return this.loginController;
    }
    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }
    //Clients
    public ClientsController getClientsController(){return this.clientsController; }
    public void setClientsController (ClientsController clientsController){this.clientsController = clientsController; }

    // Client Menu Option


    public ClientMenuController getClientMenuController() {
        return clientMenuController;
    }

    public void setClientMenuController(ClientMenuController clientMenuController) {
        this.clientMenuController = clientMenuController;
    }

    //Dashboard
    public DashboardController getDashboardController (){return this.dashboardController;}
    public void setDashboardController(DashboardController dashboardController){this.dashboardController = dashboardController;}
    //Accounts
    public AccountsController getAccountsController () {
        return this.accountsController;
    }
    public void setAccountsController(AccountsController accountsController) {
        this.accountsController = accountsController;
    }

    //Profile

    public ProfileController getProfileController() {
        return profileController;
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    //Transactions
    public TransactionsController getTransactionsController() {
        return this.transactionsController;
    }
    public void setTransactionsController(TransactionsController transactionsController) {
        this.transactionsController = transactionsController;
    }

    //TransactionCell
    public TransactionCellController getTransactionCellController() {
        return transactionCellController;
    }
    public void setTransactionCellController(TransactionCellController transactionCellController) {
        this.transactionCellController = transactionCellController;
    }

    //Receipts
    public ReceiptController getReceiptController() {
        return receiptController;
    }
    public void setReceiptController(ReceiptController receiptController) {
        this.receiptController = receiptController;
    }
    //SignUpList
    public SignUpListController getSignUpListController(){
        return this.signUpListController ;
    }
    public void setSignUpListController (SignUpListController signUpListController){
        this.signUpListController = signUpListController;
    }
    //SignUp
    public SignUpController getSignUpController() {
        return signUpController;
    }
    public void setSignUpController(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    //Search Check_Sav
    public DepositController getDepositController (){
        return this.depositController;
    }
    public void setDepositController(DepositController depositController){
        this.depositController = depositController;
    }




    // Admin View
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane createClientView;
    private AnchorPane clientsView;
    private AnchorPane depositView;
    private AnchorPane signUpListView;




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
    public AnchorPane getDashboardView(String Id){

        try {
            dashboardView = new FXMLLoader(getClass().getResource("/FXML/Client/Dashboard.fxml")).load();
            getDashboardController().setClientId(Id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dashboardView;
    }
    public AnchorPane getTransactionsView(String Id){

            try {
                transactionsView = new FXMLLoader(getClass().getResource("/FXML/Client/Transactions.fxml")).load();
                getTransactionsController().setClientId(Id);
            } catch (Exception e){
                e.printStackTrace();
            }

        return transactionsView;
    }
    public AnchorPane getAccountsView(String Id){

        try {
            accountsView = new FXMLLoader(getClass().getResource("/FXML/Client/Accounts.fxml")).load();
            getAccountsController().setClientId(Id);
        } catch (Exception e){
            e.printStackTrace();
        }

        return accountsView;
    }
    public AnchorPane getProfileView(String Id){

            try {
                profileView = new FXMLLoader(getClass().getResource("/FXML/Client/Profile.fxml")).load();
                getProfileController().setClientId(Id);
            }catch (Exception e){
                e.printStackTrace();
            }

        return profileView;
    }

    public void showClientWindow(String Id){
        ClientSession clientSession = new ClientSession(Id);
        clientSessions.put(Id, clientSession);
        System.out.println("Client Sessions:");
        clientSessions.forEach((key, value) -> {
            System.out.println("Client ID: " + key + ", Client Session: " + value);
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Client/Client.fxml"));
        ClientController clientController = new ClientController(clientSession);
        loader.setController(clientController);
        createStage(loader);
    }
    public ClientSession getClientSession(String clientId) {
        clientSessions.get(clientId);
        System.out.println("Client ID: " + clientId + ", Client Session: " + clientSessions.get(clientId));
        return clientSessions.get(clientId);
    }

    public void showSignUpWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Client/SignUp.fxml"));
        SignUpController signUpController = new SignUpController();
        loader.setController(signUpController);
        createStage(loader);
    }
    public void showForgotPassWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Client/ForgotPass.fxml"));
        ForgotPassController Controller = new ForgotPassController();
        loader.setController(Controller);
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
                signUpListView = new FXMLLoader(getClass().getResource("/FXML/Admin/SignUpList.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return signUpListView;
    }

    public AnchorPane getReceiptView(){
        if (createClientView == null){
            try {
                createClientView = new FXMLLoader(getClass().getResource("/FXML/Admin/Receipts.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return createClientView;
    }

    public AnchorPane getClientsView() {
        if (clientsView == null){
            try {
                clientsView = new FXMLLoader(getClass().getResource("/FXML/Admin/Clients.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return clientsView;
    }

    public AnchorPane getDepositView() {
        if (depositView == null){
            try {

                depositView = new FXMLLoader(getClass().getResource("/FXML/Admin/Deposit.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return depositView;
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        Stage stage = createStage(loader);
        stage.setOnCloseRequest(event -> {
            // Stop the server
            Model.getInstance().stopServer();
            // Exit the application
            System.exit(0);
        });
    }




    // Show Window
    public void showLoginWindow (){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
        createStage(loader);
    }

    private Stage createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/assets/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("ACBank");
        stage.show();
        return stage;
    }

    public void closeStage(Stage stage){
        stage.close();
    }

}
