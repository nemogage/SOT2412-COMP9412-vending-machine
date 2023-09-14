package VendingMachine.pages;

import java.util.ArrayList;

import com.opencsv.CSVWriter;

import VendingMachine.SceneManager;
import VendingMachine.pages.Page;
import javafx.scene.Scene; // check
import javafx.scene.control.Label; // check
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font; // check
import javafx.scene.text.FontWeight; // check
import javafx.stage.Stage; // check
import javafx.geometry.Pos;
import javafx.scene.control.*;
import java.io.*;

public class SellerPortal extends Page {

    private Scene scene;
    private SceneManager sm;
    
    private final int width = 1280;
    private final int height = 720;
    
    private StackPane pane;
    private Stage window;

    public Scene manageItems;
    private Scene generateList;
    private Scene generateSummary;

    private StackPane manageItemsPane;
    private StackPane generateListPane;
    private StackPane generateSummaryPane;


    /**
     * Constructor for Seller Portal page
     * @param sm
     */
    public SellerPortal(SceneManager sm) {

        this.sm = sm;
        createMainPage();
        createManageItems();
        // createGenerateList();
        //createGenerateSummary();

        // window.setScene(manageItems);

    }


    /**
     * Constructor for main page
     */
    private void createMainPage() {
        pane = new StackPane();
        scene = new Scene(pane, width, height);

        VBox buttons = new VBox(10);
        buttons.setSpacing(5);
        buttons.setPrefWidth(190.00);
        buttons.setAlignment(Pos.CENTER);
        // var pane = new Pane();
        // pane.setHgap(10);

        Button bn1 = new Button("Manage Items");
        // bn1.relocate(0, 50);
        Button bn2 = new Button("Generate List of Available items");
        Button bn3 = new Button("Generate Summary");
        Button bn4 = new Button("Return to Default Page");

        // bn4.setOnAction(e -> {
        //     app.switchScenes(app.getSceneManager().getDefaultPageScene());
        // });
        //
        // scene = new Scene(pane, width, height);

        Label lbl = new Label("Seller Portal");
        lbl.setTranslateY(20);
        lbl.setFont(Font.font("Serif", FontWeight.NORMAL, 20));

        pane.setAlignment(lbl, Pos.TOP_CENTER);
        pane.setAlignment(bn4, Pos.BOTTOM_LEFT);

        // bn4.setTranslateX(-550);
        // bn4.setTranslateY(320);
        //
        // lbl.relocate(0, 30);

        buttons.getChildren().addAll(bn1, bn2, bn3);
        buttons.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);
        pane.getChildren().add(buttons);
        pane.getChildren().addAll(lbl, bn4);
        bn1.setOnAction(e -> this.sm.switchScenes(manageItems));
        bn2.setOnAction(e -> createItemSummary());
        bn3.setOnAction(e -> createGenerateSummary());
        bn4.setOnAction(e -> this.sm.switchScenes(this.sm.getDefaultPageScene())) ;
    }

    /**
     * Function to create the 'manage items' feature
     */
    private void createManageItems() {
        manageItemsPane = new StackPane();
        manageItems = new Scene(manageItemsPane, width, height);

        Button bn = new Button("Return to Seller Portal");

        Label lbl = new Label("Manage Items Portal");
        lbl.setFont(Font.font("Serif", FontWeight.NORMAL, 20));

        manageItemsPane.setAlignment(lbl, Pos.TOP_CENTER);
        lbl.setTranslateY(20);
        // generateListPane.setAlignment(bn, Pos.BOTTOM_LEFT);

        bn.setTranslateX(-550);
        bn.setTranslateY(320);

        lbl.relocate(0, 30);

        manageItemsPane.getChildren().addAll(lbl, bn);
        bn.setOnAction(e -> this.sm.switchScenes(scene));
    }

    private void createItemSummary() {

        sm.getDatabase().openConn();
        ArrayList<String[]> items = sm.getDatabase().getAllItems();
        sm.getDatabase().closeConn();

        File file = new File("reports/itemsReport.csv");

        // attempt to delete the file
        try {
            file.delete();
        } catch (Exception e) {
            System.out.println("Generating new file...");
        }

        try {
            // Create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file, true);

            // Create CSVWriter object file writer object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            // Add header to ownerUsersSummary.csv if empty
            if (file.length() == 0) {
                String[] header = {"ITEM_NAME", "CATEGORY"};
                writer.writeNext(header);
            }

            // Write to .csv
            for (String[] item : items) {
                writer.writeNext(item);
            }

            writer.close();
            outputFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert successfulRegisterAlert = new Alert(Alert.AlertType.INFORMATION);
        successfulRegisterAlert.setTitle("Success");
        successfulRegisterAlert.setHeaderText(String.format("Summary generation successful!"));
        successfulRegisterAlert.setContentText("You view the summary of items as a csv.");
        successfulRegisterAlert.showAndWait();

    }

    /**
     * Function to create the 'generate summary' feature
     */
    private void createGenerateSummary() {
        sm.getDatabase().openConn();
        ArrayList<String[]> items = sm.getDatabase().getItemSoldHistory();
        sm.getDatabase().closeConn();

        File file = new File("reports/itemSoldHistoryReport.csv");

        // attempt to delete the file
        try {
            file.delete();
        } catch (Exception e) {
            System.out.println("Generating new file...");
        }

        try {
            // Create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file, true);

            // Create CSVWriter object file writer object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            // Add header to ownerUsersSummary.csv if empty
            if (file.length() == 0) {
                String[] header = {"ITEM_CODE", "ITEM_NAME", "CATEGORY_NAME", "QUANTITY", "PRICE", "QUANTITY_SOLD"};
                writer.writeNext(header);
            }

            // Write to .csv
            for (String[] item : items) {
                writer.writeNext(item);
            }

            writer.close();
            outputFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert successfulRegisterAlert = new Alert(Alert.AlertType.INFORMATION);
        successfulRegisterAlert.setTitle("Success");
        successfulRegisterAlert.setHeaderText(String.format("Summary generation successful!"));
        successfulRegisterAlert.setContentText("You view all items and the number sold as a csv.");
        successfulRegisterAlert.showAndWait();

    }

    /**
     * Function to return the scene
     */
    public Scene getScene() {
        return this.scene;
    }

}
