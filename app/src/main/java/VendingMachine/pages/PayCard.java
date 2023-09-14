package VendingMachine.pages;

import VendingMachine.*;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.util.*;

// JSON Reader
import org.json.simple.*;
import org.json.simple.parser.*;

// OpenCSV import
import com.opencsv.*;

public class PayCard extends Page {
    
    private SceneManager sceneManager;


    /**
     * Constructor for PayCard page
     * @param sceneManager
     */
    public PayCard(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }


    /**
     * Builds the scene for the PayCard page
     */
    public void setScene() {

        GridPane grid = new GridPane();

        String username = sceneManager.getSession().getUserName();

        String total = Double.toString(sceneManager.getSession().getTransaction().getTotal());
        
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.setAlignment(Pos.CENTER); 
        scene = new Scene(grid, WIDTH, HEIGHT);

        Text scenetitle = new Text("Card Payment");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label cardNumberLabel = new Label("Card No:");
        grid.add(cardNumberLabel, 0, 1);

        TextField cardNumberTextField = new TextField();
        grid.add(cardNumberTextField, 1, 1);

        Label cvvLabel = new Label("CVV:");
        grid.add(cvvLabel, 0, 2);

        PasswordField cvvBox = new PasswordField();
        grid.add(cvvBox, 1, 2);

        sceneManager.getDatabase().openConn();
        String[] details = sceneManager.getDatabase().getCard(username);

        // If card details exist for this user, autofill
        if (cardExists(username)) {

            System.out.println(details[0] + " " + details[1]);

            if (details != null) {
                cardNumberTextField = new TextField(details[0]);
                grid.add(cardNumberTextField, 1, 1);
            }
            cvvBox = new PasswordField();
        }

        Button payButton = new Button("Pay");

        cvvBox.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                payButton.fire();
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        grid.add(hbBtn, 1, 4);



        final TextField cardNumberTextFieldAAA = cardNumberTextField;
        final PasswordField cvvBoxAAA = cvvBox;

        payButton.setOnAction(e -> {
            sceneManager.getDatabase().openConn();

            String cardNumber = cardNumberTextFieldAAA.getText();
            String cvv = cvvBoxAAA.getText();

            boolean checkedCardNumber = checkCardNumber(cardNumber);
            boolean checkedCVV = checkCVV(cvv);

            boolean checkExistsCard = false;
            if (! cardNumber.isEmpty())
                checkExistsCard = checkJSON(Integer.parseInt(cardNumber));

            // Write to transactions.csv if valid
            if (checkExistsCard == false) {
                Alert unrecognisedCardNumberAlert = new Alert(AlertType.ERROR);
                unrecognisedCardNumberAlert.setTitle("Invalid card number.");
                unrecognisedCardNumberAlert.setHeaderText("The card number inputted is not in the recognised list of card numbers.");
                unrecognisedCardNumberAlert.setContentText("Please try again, or contact the operator for assistance.");
                unrecognisedCardNumberAlert.showAndWait();
                return;
            }
            else if (checkedCardNumber == false) {
                Alert invalidCardNumberAlert = new Alert(AlertType.ERROR);
                invalidCardNumberAlert.setTitle("Invalid card number.");
                invalidCardNumberAlert.setHeaderText("The card number inputted is invalid.");
                invalidCardNumberAlert.setContentText("Please try again.");
                invalidCardNumberAlert.showAndWait();
                return;
            }
            else if (checkedCVV == false) {
                Alert incorrectCVVAlert = new Alert(AlertType.ERROR);
                incorrectCVVAlert.setTitle("Incorrect CVV");
                incorrectCVVAlert.setHeaderText("The CVV inputted was invalid.");
                incorrectCVVAlert.setContentText("Please try again.");
                incorrectCVVAlert.showAndWait();
                return;
            }
            else if (checkExistsCard == false) {
                Alert incorrectCVVAlert = new Alert(AlertType.ERROR);
                incorrectCVVAlert.setTitle("Card does not exist");
                incorrectCVVAlert.setHeaderText("The card number inserted is not in the list of accepted card numbers.");
                incorrectCVVAlert.setContentText("Please try again with a different card.");
                incorrectCVVAlert.showAndWait();
                return;
            }
            else if (checkedCardNumber == true && checkedCVV == true && checkExistsCard == true) {
                writeTransaction(username, cardNumber, cvv, total);
                Alert paymentSuccessfulAlert = new Alert(AlertType.ERROR);
                paymentSuccessfulAlert.setTitle("Success!");
                paymentSuccessfulAlert.setHeaderText("Your payment of $" + total + " was a success.");
                paymentSuccessfulAlert.setContentText("Have a great day!");

                // Add to database
                if (! cardExists(username)) {
                    String[] tempDetails = {username, cardNumber, cvv};
                    insertCard(tempDetails);
                }

                // Clear cart
                sceneManager.getSession().getTransaction().reset();

                paymentSuccessfulAlert.showAndWait();

                // Go back to default page
                sceneManager.switchScenes(sceneManager.getSuccessfulPageScene());
            }

            // Successful payment
            System.out.println("Payment successful!");
            
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            sceneManager.switchScenes(sceneManager.getCheckoutPageScene());
        });

        // Add buttons to HBox
        hbBtn.getChildren().add(backButton);
        hbBtn.getChildren().add(payButton);

    }


    /**
     * Writes transaction details to transactions.csv file
     * @param username
     * @param cardNumber
     * @param cvv
     * @param amount
     */
    public void writeTransaction(String username, String cardNumber, String cvv, String amount) {

        File file = new File("reports/transactions.csv");
        try {
            // Create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file, true);
    
            // Create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
    
            // Add header to transactions.csv if empty
            if (file.length() == 0) {
                String[] header = {"USERNAME", "CARD_NUMBER", "CVV", "TRANSACTION_AMOUNT"};
                writer.writeNext(header);
            }
    
            // Add data to transactions.csv
            String[] data = {
                username, 
                cardNumber, 
                cvv,
                amount
            };
            
            writer.writeNext(data);
    
            // Closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Transactions file not found!");
        }
    }


    /**
     * Checks if card number is valid.
     * @param cardNumber
     */
    public static boolean checkCardNumber(String cardNumber) {

        long number;

        // Checks if long
        try {
            number = Long.parseLong(cardNumber);
        } catch (Exception e) {
            return false;
        }

        // Checks if less than 16 digits long
        int numDigits = String.valueOf(number).length();
        if (numDigits > 16 && numDigits > 0)
            return false;

        // If all conditions are met
        return true;
    }


    /**
     * Checks if CVV is valid.
     * @param CVV
     */
    public static boolean checkCVV(String CVV) {

        int number;

        // Checks if integer
        try {
            number = Integer.parseInt(CVV);
        } catch (Exception e) {
            return false;
        }

        // Checks if 3 digits long
        int numDigits = String.valueOf(number).length();
        if (numDigits != 3)
            return false;

        // If all conditions are met
        return true;
    }


    /**
     * Insert user and card details in cards table in database
     * @param details
     */
    public void insertCard(String[] details) {
        sceneManager.getDatabase().openConn();

        int returned = sceneManager.getDatabase().insertNewCard(
            details[0], 
            details[1], 
            details[2]
            );
    }


    /**
     * Check if card details exist in cards table
     * @return
     */
    public boolean cardExists(String username) {
        sceneManager.getDatabase().openConn();
        String[] persistCard = sceneManager.getDatabase().getCard(username);

        if (persistCard == null) {
            return false;
        }
        else {
            System.out.println("Card details found for user: " + username);
            return true;
        }
    }


    /**
     * Checks if the card number exists in the credit_cards.json file.
     * @param cardNumber
     * @return true if card is recognised, false if card is not recognised
     */
    public boolean checkJSON(int cardNumber) {

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader("json/credit_cards.json")) {

            String cardNumberString = Integer.toString(cardNumber);

            // Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONArray detailsList = (JSONArray) obj;
            // System.out.println(detailsList);

            ArrayList<String> numbers = new ArrayList<String>();     

            for (int i = 0; i < detailsList.size(); i++) {
                JSONObject perf = (JSONObject) detailsList.get(i);
                String name = (String) perf.get("name");
                String number = (String) perf.get("number");
                numbers.add(number);
            }

            for (String number : numbers) {   

                // System.out.println(numbers.get(i));

                if (cardNumberString.equals(number)) {
                    return true;
                }

            }

            return false;
 
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            return false;
        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        } catch (ParseException e) {
            // e.printStackTrace();
            return false;
        }

    }


    /**
     * Returns the PayCard scene.
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }

}