package VendingMachine;

import VendingMachine.controllers.DefaultPageController;
import VendingMachine.pages.*;
import javafx.scene.*;
import javafx.stage.Stage;

public class SceneManager {

    private Database database;
    private Stage stage;

    private OwnerPortal ownerPortal;
    private Scene defaultPage;
    private CashierPortal cashierPortal;
    private SellerPortal sellerPortal;
    private Login login;
    private CheckoutPage checkoutPage;

    private InputCashPage inputCashPage;
    private SuccessfulPage successfulPage;
    private Session session;

    private DefaultPageController defaultPageController;

    protected String reportsDirectory = "app/src/resources/reports/";

    /**
     * Constructor for SceneManager
     */
    public SceneManager() {
        setUp();
    }


    /**
     * Setup method for SceneManager
     */
    public void setUp() {
        database = new Database();
        session = new Session();
        session.getTransaction().setSceneManager(this);
        ownerPortal = new OwnerPortal(this);
        cashierPortal = new CashierPortal(this);
        sellerPortal = new SellerPortal(this);
        login = new Login(this);
        inputCashPage = new InputCashPage(this);
        successfulPage = new SuccessfulPage(this);
    }


    /**
     * Method to switch the current scene
     * @param scene
     */
    public void switchScenes(Scene scene) {
        stage.setScene(scene);
    }


    /**
     * Getter method for scene
     * @return stage.getScene()
     */
    public Scene getScene() {
        return stage.getScene();
    }


    /**
     * Setter method for stage
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    /**
     * Setter method for default page
     * @param defaultPage
     */
    public void setDefaultPage(Scene defaultPage) {
        this.defaultPage = defaultPage;
    }


    /**
     * Getter method for default page scene
     * @return defaultPage
     */
    public Scene getDefaultPageScene() {
        return defaultPage;
    }

    /**
     * Getter method for cleared default page scene
     * @return defaultPage
     */
    public Scene getClearedDefaultPageScene() {
        defaultPageController.logout();
        return defaultPage;
    }

    /**
     * Getter method for owner portal scene
     * @return ownerPortal.getScene()
     */
    public Scene getOwnerPortalScene() {
        return ownerPortal.getScene();
    }


    /**
     * Getter method for cashier portal scene
     * @return cashierPortal.getScene()
     */
    public Scene getCashierPortalScene() {
        return cashierPortal.getScene();
    }


    /**
     * Getter method for seller portal scene
     * @return sellerPortal.getScene()
     */
    public Scene getSellerPortalScene() {
        return sellerPortal.getScene();
    }


    /**
     * Getter method for login scene
     * @return login.getScene()
     */
    public Scene getLoginScene() {
        return login.getScene();
    }


    /**
     * Getter method for successful page
     * @return successfulPage
     */
    public SuccessfulPage getSuccessfulPage() {
        return successfulPage;
    }


    /**
     * Getter method and instantiator for checkout scene
     * @return checkoutPage.getScene()
     */
    public Scene getCheckoutPageScene() {
        checkoutPage = new CheckoutPage(this);
        return checkoutPage.getScene();
    }


    /**
     * Getter method for successful page scene
     * @return successfulPage.getScene()
     */
    public Scene getSuccessfulPageScene() {
        return successfulPage.getScene();
    };


    /**
     * Getter method for input cash page scene
     * @return inputCashPage.getScene()
     */
    public Scene getInputCashPageScene() {
        return inputCashPage.getScene();
    }


    /**
     * Getter method for database
     * @return database
     */
    public Database getDatabase() {
        return database;
    }


    /**
     * Getter method for session
     * @return session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Setter method for default page controller
     * @param defaultPageController
     */
    public void setDefaultPageController(DefaultPageController defaultPageController) {
        this.defaultPageController = defaultPageController;
    }


    /**
     * Getter method for default page controller
     * @return default page controller
     */
    public DefaultPageController getDefaultPageController() {
        return defaultPageController;
    }
}
