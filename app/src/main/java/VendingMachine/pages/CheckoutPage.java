package VendingMachine.pages;

import VendingMachine.SceneManager;

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

// Imports for timer
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.SECONDS;

// Imports for time
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// OpenCSV import
import com.opencsv.*;


public class CheckoutPage extends Page {

    private Pane pane;
    private SceneManager sm;

    private Button payCard;
    private Button payCash;
    private Button returnToDp;

    private Button cancelTransactionButton;

    private Scene payCardPage;
    private Scene payCashPage;

    /**
     * The Constructor for the Checkout Page, sets the scene for the checkout page.
     * @param sceneManager
     */
    public CheckoutPage(SceneManager sceneManager) {

        sm = sceneManager;

        pane = new StackPane();
        scene = new Scene(pane, WIDTH, HEIGHT);

        this.createPayCash();

        VBox box = new VBox();
        box.setSpacing(5);
        box.setPrefWidth(190.00);
        box.setAlignment(Pos.CENTER);

        payCard = new Button("Pay by Card");
        payCash = new Button("Pay by Cash");

        cancelTransactionButton = new Button("Cancel Transaction");
        cancelTransactionButton.setOnAction(e -> {
            cancelTransaction("Manual");
        });
        cancelTransactionButton.setMinWidth(box.getPrefWidth());
        cancelTransactionButton.setAlignment(Pos.CENTER);

        returnToDp = new Button("Return to default page");

        returnToDp.setOnAction(e -> sm.switchScenes(sm.getDefaultPageScene()));

        payCard.setMinWidth(box.getPrefWidth());
        payCash.setMinWidth(box.getPrefWidth());

        returnToDp.setTranslateX(-550);
        returnToDp.setTranslateY(320);

        Text title = new Text();
        title.setText("Checkout");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 35));


        // Elements for timer
        Text timerText = new Text();
        timerText.setTranslateY(-320);
        timerText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        // Length of timer in seconds
        int refreshCountdown = 120;
        IntegerProperty countDown = new SimpleIntegerProperty(refreshCountdown);

        countDown.addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                
                // When timer is decremented

                int time = newValue.intValue();
                timerText.setText("Time left: " + Integer.toString(time));

                if (time > 0) {
                    // System.out.println(time);
                    if (time == (int) refreshCountdown / 2) {
                        System.out.println(Integer.toString(time) + " seconds left before transaction is cancelled.\n");
                        timerText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                    }
                }
                else {

                    Scene currentScene = sm.getScene();
                    Scene thisScene = scene;

                    if (currentScene == scene) {
                        cancelTransaction("Timer");

                        Alert timeoutAlert = new Alert(AlertType.ERROR);
                        timeoutAlert.setTitle("Time's up!");
                        timeoutAlert.setHeaderText("The time limit has passed.");
                        timeoutAlert.setContentText("Your transaction has been cancelled due to exceeding the time limit of 2 minutes.\n If you were logged in, you have been logged out.");
                        Platform.runLater(timeoutAlert::showAndWait);
                    }

                }

            }

        });

        final Timeline timeToRefresh = new Timeline();
        timeToRefresh.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(countDown, refreshCountdown)),
                new KeyFrame(Duration.seconds(refreshCountdown), new KeyValue(countDown, 0)));
        timeToRefresh.playFromStart();

        // Adding child object references to parent objects
        box.getChildren().addAll(title, payCard, payCash, cancelTransactionButton, timerText);
        pane.getChildren().add(box);
        pane.getChildren().add(returnToDp);

        // 'Pay by card' button
        PayCard payCardPage = new PayCard(sm);

        payCard.setOnAction(e -> {
            payCardPage.setScene();
            sm.switchScenes(payCardPage.getScene());
        });

        payCash.setOnAction(e -> {
            sm.switchScenes(sm.getInputCashPageScene());;
        });

    }

    
    /**
     * Method to log user out if transaction is cancelled manually or by timeout
     * @param reason
     */
    public void cancelTransaction(String reason) {

        sm.switchScenes(sm.getDefaultPageScene());
        System.out.println("Transaction cancelled. User logged out.\n");

        File file = new File("reports/ownerCancelledTransactionsSummary.csv");
        try {
            // Create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file, true);
    
            // Create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
    
            // Add header to transactions.csv if empty
            if (file.length() == 0) {
                String[] header = {"DATE & TIME", "USER", "REASON"};
                writer.writeNext(header);
            }

            // Get date and time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            System.out.println(dtf.format(now));

            // Get username
            String username = sm.getSession().getUserName();
            if (username.equals("Guest")) {
                username = "Anonymous";
            }
    
            // Add data to cancelledTransactions.csv
            String[] data = {
                dtf.format(now), // Date and time
                username,
                reason
            };

            sm.getDefaultPageController().logout();
            
            writer.writeNext(data);
    
            // Closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cancelled transactions file not found!");
        }

    }


    /**
     * Method to build createPayCash scene for cash payments
     */
    public void createPayCash() {
        StackPane pane = new StackPane();
        payCashPage = new Scene(pane, WIDTH, HEIGHT);

        Button bn = new Button("Return to checkout page");

        Label lbl = new Label("Pay by cash");
        lbl.setFont(Font.font("Serif", FontWeight.NORMAL, 20));

        lbl.setTranslateY(20);

        bn.setTranslateX(-550);
        bn.setTranslateY(320);

        lbl.relocate(0, 30);

        pane.getChildren().addAll(lbl, bn);
        bn.setOnAction(e -> sm.switchScenes(sm.getCheckoutPageScene()));
    }

}
