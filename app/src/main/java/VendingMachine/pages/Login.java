package VendingMachine.pages;

import VendingMachine.SceneManager;
import VendingMachine.pages.Page;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;

import java.util.Optional;

public class Login extends Page {

    private SceneManager sceneManager;
    private GridPane grid;

    private Button signInButton;
    private Button registerButton;
    private Button backButton;

    private HBox hbBtn;
    private Text scenetitle;
    private Label usernameLabel;
    private TextField userTextField;
    private Label pwLabel;
    private PasswordField pwBox;

    private String loginUsername;
    private String loginPassword;

    private Label registerUsernameLabel;
    private Label registerPasswordLabel;
    private TextField registerUsernameText;
    private PasswordField registerPasswordText;

    private String registerUsername;
    private String registerPassword;

    public Login(SceneManager sceneManager) {
        
        this.sceneManager = sceneManager;

        setupScene();
        setupLoginForm();
        setupActions();

    }

    /**
     * Setup scene for login page
     */
    private void setupScene() {
        grid = new GridPane();
        grid.setGridLinesVisible(false);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.setAlignment(Pos.CENTER); 

        super.scene = new Scene(grid, WIDTH, HEIGHT);
    }


    /**
     * Setup login form
     */
    private void setupLoginForm() {
        scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        usernameLabel = new Label("User Name:");
        grid.add(usernameLabel, 0, 1);

        userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        pwLabel = new Label("Password:");
        grid.add(pwLabel, 0, 2);

        pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        // setup hbox for buttons
        hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbBtn, 1, 4);

        backButton = new Button("Back");
        hbBtn.getChildren().add(backButton);
        
        registerButton = new Button("Register");
        hbBtn.getChildren().add(registerButton);
        
        signInButton = new Button("Sign In");
        hbBtn.getChildren().add(signInButton);
    }


    /**
     * Setup actions for login page
     */
    private void setupActions() {

        // ACTION 1: if user presses enter key on password box, it fires the sign in button
        pwBox.setOnKeyPressed(
            e -> {
                if (e.getCode().equals(KeyCode.ENTER))
                    signInButton.fire();
            }
        );

        // ACTION 2: if user presses login button, attempt to login
        signInButton.setOnAction(e -> {
            attemptLogin();
        });

        // ACTION 3: if user presses back button, return to default page
        backButton.setOnAction(e -> {
            sceneManager.switchScenes(sceneManager.getDefaultPageScene());
        });

        // ACTION 4: if user presses register button, show register pop up
        registerButton.setOnAction(e -> {
            displayRegisterDialog();
        });

    }

    /**
     * Configure attempts to login
     */
    private void attemptLogin() {
        // set username and password variables
        loginUsername = userTextField.getText();
        loginPassword = pwBox.getText();

        // if user has inputted nothing, return error immediately
        if (loginUsername == null || loginPassword == null) {
            showNullEntryError();
            return;
        }

        // otherwise check if the username is valid
        sceneManager.getDatabase().openConn();
        int validUsername = sceneManager.getDatabase().validateUsername(loginUsername);
        sceneManager.getDatabase().closeConn();
        if (validUsername == -1) {                
            showUsernameError();
            return;
        }
        
        // if username is valid, try and log in
        sceneManager.getDatabase().openConn();
        int validLogin = sceneManager.getDatabase().login(loginUsername, loginPassword);
        sceneManager.getDatabase().closeConn();
        if (validLogin == -1) {
            showIncorrectPasswordError(); 
            return;           
        }

        /* SUCCESSFUL LOGIN */

        // clear the text boxes
        userTextField.clear();
        pwBox.clear();
        
        // update session
        sceneManager.getDatabase().openConn();
        String role = sceneManager.getDatabase().getRole(loginUsername);
        sceneManager.getDatabase().closeConn();

        sceneManager.getSession().resetSession();
        sceneManager.getSession().setLoggedIn(true);
        sceneManager.getSession().setUserName(loginUsername);
        sceneManager.getSession().setRole(role);

        // change back to the defualt page, but this time its logged in
        sceneManager.getDefaultPageController().login();
        sceneManager.switchScenes(sceneManager.getDefaultPageScene()); 
        
    }

    /**
     * Method to show error when no input is made into a corresponding field
     */
    private void showNullEntryError() {
        Alert nullUsernameAlert = new Alert(AlertType.ERROR);
        nullUsernameAlert.setTitle("No input entered");
        nullUsernameAlert.setHeaderText("You have not entered any input.");
        nullUsernameAlert.setContentText("Please try again.");
        nullUsernameAlert.showAndWait();

        return;
    }

    /**
     * Method to format incorrect username error message
     */
    private void showUsernameError() {
        Alert invalidUsernameAlert = new Alert(AlertType.ERROR);
        invalidUsernameAlert.setTitle("Invalid username");
        invalidUsernameAlert.setHeaderText(String.format("A user with username '%s' does not exist!", loginUsername));
        invalidUsernameAlert.setContentText("Please try again.");
        invalidUsernameAlert.showAndWait();

        return;
    }

    /**
     * Method to format incorrect password error message
     */
    private void showIncorrectPasswordError() {
        Alert incorrectPassAlert = new Alert(AlertType.ERROR);
        incorrectPassAlert.setTitle("Incorrect Password");
        incorrectPassAlert.setHeaderText("Your password is incorrect!");
        incorrectPassAlert.setContentText("Please try again.");
        incorrectPassAlert.showAndWait();
        return;
    }

    /**
     * Method to display register dialog
     */
    private void displayRegisterDialog() {
        // initialise dialog pane
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Register");
        dialog.setHeaderText("Register a Customer Account");
        dialog.setResizable(false);
        
        // widgets
        registerUsernameLabel = new Label("Username: ");
        registerPasswordLabel = new Label("Password: ");
        registerUsernameText = new TextField();
        registerPasswordText = new PasswordField();
                
        // create layout and add to dialog
        GridPane gridRegister = new GridPane();
        gridRegister.setHgap(10);
        gridRegister.setVgap(10);
        gridRegister.setPadding(new Insets(0, 10, 0, 10));

        gridRegister.add(registerUsernameLabel, 1, 1);
        gridRegister.add(registerUsernameText, 2, 1);
        gridRegister.add(registerPasswordLabel, 1, 2);
        gridRegister.add(registerPasswordText, 2, 2);
        dialog.getDialogPane().setContent(gridRegister);
        
        // add button to dialog
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeOk = new ButtonType("Register", ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        
        // show dialog
        Optional<ButtonType> result = dialog.showAndWait();

        // if either CANCEL or REGISTER button is pressed
        if (result.isPresent()) {
            ButtonType btResult = result.get();
            if (btResult.getText() == "Cancel") {
                return;
            } else if (btResult.getText() == "Register") {
                this.registerUsername = registerUsernameText.getText();
                this.registerPassword = registerPasswordText.getText();

                // attempt to register user
                this.sceneManager.getDatabase().openConn();
                int validRegister = this.sceneManager.getDatabase().insertNewUser(registerUsername, registerPassword, "REGISTERED CUSTOMER");
                
                if (validRegister == -1) {
                    showRegisterError();
                } else {
                    showRegisterSuccess();
                }
                
                this.sceneManager.getDatabase().closeConn();
                return;
            }
        }
    }

    private void showRegisterError() {
        Alert invalidRegisterError = new Alert(AlertType.ERROR);
        invalidRegisterError.setTitle("Registration failed");
        invalidRegisterError.setHeaderText(String.format("An error occured during registration."));
        invalidRegisterError.setContentText("Please try again. Check if your username and password are <= 15 characters.");
        invalidRegisterError.showAndWait();

        return;
    }

    private void showRegisterSuccess() {
        Alert successfulRegisterAlert = new Alert(AlertType.INFORMATION);
        successfulRegisterAlert.setTitle("Success");
        successfulRegisterAlert.setHeaderText(String.format("Registration successful!"));
        successfulRegisterAlert.setContentText("You may now log in as a customer.");
        successfulRegisterAlert.showAndWait();

        return;
    }

    public Scene getScene() {
        return this.scene;
    }

}
