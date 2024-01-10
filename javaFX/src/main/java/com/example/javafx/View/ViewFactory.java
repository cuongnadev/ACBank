package com.example.javafx.View;

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
        this.loginAccountType = AccountType.CLIENT;
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
    private ForgotPassListController forgotPassListController;
    private ClientsController clientsController;
    private DashboardController dashboardController;
    private AccountsController accountsController;
    private ProfileController profileController;
    private TransactionsController transactionsController;
    private ReceiptController receiptController;
    private SignUpController signUpController;
    private SignUpListController signUpListController;
    private Check_SavingsListController check_savingsListController;

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
    public Check_SavingsListController getCheck_SavingsListController (){
        return this.check_savingsListController;
    }
    public void setCheck_savingsListController(Check_SavingsListController check_savingsListController){
        this.check_savingsListController = check_savingsListController;
    }



    // Admin View
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane createClientView;
    private AnchorPane clientsView;
    private AnchorPane savingsListView;
    private AnchorPane signUpListView;




    public AccountType getLoginAccountType(){
        return loginAccountType;
    }
    public void setLoginAccountType(AccountType loginAccountType){
        this.loginAccountType = loginAccountType;
    }

    public ForgotPassListController getForgotPassListController() {
        return forgotPassListController;
    }

    public void setForgotPassListController(ForgotPassListController forgotPassListController) {
        this.forgotPassListController = forgotPassListController;
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
    public AnchorPane getProfileView(){
        if (profileView == null){
            try {
                profileView = new FXMLLoader(getClass().getResource("/Fxml/Client/Profile.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return profileView;
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
    public void showForgotPassWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/ForgotPass.fxml"));
        ForgotPassController Controller = new ForgotPassController();
        loader.setController(Controller);
        createStage(loader);
    }
    public void showForgotPassList(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ForgotPassList.fxml"));
        ForgotPassListController Controller = new ForgotPassListController();
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

    public AnchorPane getCheck_SavingsListView() {
        if (savingsListView == null){
            try {
                savingsListView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Check_SavingsList.fxml")).load();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return savingsListView;
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
