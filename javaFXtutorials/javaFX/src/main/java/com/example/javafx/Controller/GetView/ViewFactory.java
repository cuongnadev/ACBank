package com.example.javafx.Controller.GetView;

import com.example.javafx.Controller.Admin.*;
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
    public ViewFactory(){
        this.loginAccountType = AccountType.ADMIN;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    private AccountType loginAccountType;

    //Client View
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
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
    private ReceiptController receiptController;
    private SignUpController signUpController;
    private SignUpListController signUpListController;
    private DepositController depositController;

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
    public AnchorPane getDashboardView(){
        try {
            dashboardView = new FXMLLoader(getClass().getResource("/FXML/Client/Dashboard.fxml")).load();
        } catch (Exception e){
            e.printStackTrace();
        }
        return dashboardView;
    }
    public AnchorPane getTransactionsView(){
        if (transactionsView == null){
            try {
                transactionsView = new FXMLLoader(getClass().getResource("/FXML/Client/Transactions.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return transactionsView;
    }
    public AnchorPane getAccountsView(){
        if (accountsView == null){
            try {
                accountsView = new FXMLLoader(getClass().getResource("/FXML/Client/Accounts.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return accountsView;
    }
    public AnchorPane getProfileView(){
        if (profileView == null){
            try {
                profileView = new FXMLLoader(getClass().getResource("/FXML/Client/Profile.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return profileView;
    }

    public void showClientWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
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
        createStage(loader);
    }




    // Show Window
    public void showLoginWindow (){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
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
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/assets/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("ACBank");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}
