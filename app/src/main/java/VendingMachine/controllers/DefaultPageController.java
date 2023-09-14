package VendingMachine.controllers;

import VendingMachine.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultPageController {


    private SceneManager sceneManager = new SceneManager();
    private Database database;
    private Session session;
    Font font = new Font("System", 15);

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private HashMap<String, Image> images = new HashMap<>();

    @FXML
    Label roleLabel;

    @FXML
    Label accountLabel;

    @FXML
    ScrollPane scrollPane;

    @FXML
    ScrollPane cartScrollPane;

    @FXML
    Label totalLabel;

    @FXML
    Button loginBtn;

    public DefaultPageController() {
        sceneManager.setDefaultPageController(this);
        database = sceneManager.getDatabase();
        session = sceneManager.getSession();
        loadImages();
    }

    public void loadImages() {
        int requestedWidth = 100;
        int requestedHeight = 100;
        boolean preseveRatio = false;
        boolean smooth = false;
        images.put("1001", new Image("images/Mineral Water.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("1002", new Image("images/Sprite.jpeg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("1003", new Image("images/Coca cola.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("1004", new Image("images/Pepsi.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("1005", new Image("images/Juice.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("2001", new Image("images/Mars.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("2002", new Image("images/M&M.jpeg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("2003", new Image("images/Bounty.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("2004", new Image("images/Snickers.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("3001", new Image("images/Smiths.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("3002", new Image("images/Pringles.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("3003", new Image("images/Kettle.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("3004", new Image("images/Thins.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("4001", new Image("images/Mentos.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("4002", new Image("images/Sour Patch.jpeg", requestedWidth, requestedHeight, preseveRatio, smooth));
        images.put("4003", new Image("images/Skittles.jpg", requestedWidth, requestedHeight, preseveRatio, smooth));
    }

    public void setDefaultPageAndStage(ActionEvent event) {
        sceneManager.setDefaultPage(((Node)event.getSource()).getScene());
        sceneManager.setStage((Stage)((Node)event.getSource()).getScene().getWindow());
    }

    public void proceedToPortal(ActionEvent event) {
        setDefaultPageAndStage(event);
        if(session.isLoggedIn()){

            database.openConn();
            String role = database.getRole(session.getUserName());
            database.closeConn();

            if(role.equalsIgnoreCase("owner")) {
                sceneManager.switchScenes(sceneManager.getOwnerPortalScene());
            }
            else if(role.equalsIgnoreCase("seller")) {
                sceneManager.switchScenes(sceneManager.getSellerPortalScene());
            }
            else if(role.equalsIgnoreCase("cashier")) {
                sceneManager.switchScenes(sceneManager.getCashierPortalScene());
            }
        }
        else {
            alertPleaseLoginFirst();
        }
    }

    /**
     * Method to show error
     */
    private void alertPleaseLoginFirst() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not logged in");
        alert.setHeaderText("Please login first");
        alert.setContentText("Please try again");
        alert.showAndWait();
    }

    /**
     * Method to show error
     */
    private void alertPleaseAddItemsToCartFirst() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No items in cart");
        alert.setHeaderText("Please add items to cart first");
        alert.setContentText("Please try again");
        alert.showAndWait();
    }

    public void proceedToCheckout(ActionEvent event) {
        if (sceneManager.getSession().getTransaction().getItems().size() > 0) {
            setDefaultPageAndStage(event);
            sceneManager.switchScenes(sceneManager.getCheckoutPageScene());
        }
        else {
            alertPleaseAddItemsToCartFirst();
        }

    }

    public void loginButtonAction(ActionEvent event) {
        if (!session.isLoggedIn()) {
            setDefaultPageAndStage(event);
            sceneManager.switchScenes(sceneManager.getLoginScene());
        }
        else {
            logout();
        }
    }

    public void logout() {
        session.resetSession();
        updateSessionBox();
        loginBtn.setText("Log In");
        clearTransaction();
    }

    public void login() {
        updateSessionBox();
        loginBtn.setText("Log Out");
        clearTransaction();
    }

    public void updateSessionBox() {
        roleLabel.setText("Role: " + session.getRole());
        accountLabel.setText("Account: " + session.getUserName());
    }

    public void clearTransaction() {
        session.getTransaction().reset();
        displayItemStrings(new ArrayList<>());
        updateCart();
    }


    public void displayItemStrings(ArrayList<String> itemStrings) {

        VBox items = new VBox();
        items.setSpacing(30);
        scrollPane.setContent(items);

        for (String itemCode : itemStrings) {

            HBox item = new HBox();
            item.setPadding(new Insets(10));
            HBox.setMargin(item, new Insets(50));
            item.setPrefSize(500, 100);

            database.openConn();
            String itemName = database.queryItemName(itemCode);
            Double itemPrice = database.queryItemPrice(itemCode);
            int itemQuantity = database.queryItemQuantity(itemCode);
            database.closeConn();

            Label nameLabel = new Label(itemName);
            nameLabel.setFont(font);

            Region region0 = new Region();
            HBox.setHgrow(region0, Priority.ALWAYS);

            Region region1 = new Region();
            HBox.setHgrow(region1, Priority.ALWAYS);

            Label priceLabel = new Label("$" + df.format(itemPrice));
            priceLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

            Region region2 = new Region();
            HBox.setHgrow(region2, Priority.ALWAYS);

            Label quantityLabel = new Label("Stock: " + itemQuantity);
            quantityLabel.setFont(font);

            Region region3 = new Region();
            HBox.setHgrow(region3, Priority.ALWAYS);

            Button button = new Button("Add to Cart");
            button.setFont(font);

            button.setOnAction(event -> {
                session.getTransaction().addItem(itemCode);
                updateCart();
            });

            ImageView imageView = new ImageView();
            imageView.setImage(images.get(itemCode));

            item.getChildren().addAll(
                    imageView,
                    region0,
                    nameLabel,
                    region1,
                    priceLabel,
                    region2,
                    quantityLabel,
                    region3,
                    button
            );

            item.setStyle("-fx-background-color:#98aded");
            items.getChildren().add(item);

        }

    }

    public void displayRecentlyBought(ActionEvent event) {

        database.openConn();
        ArrayList<String> itemStrings = database.queryRecent();
        database.closeConn();

        displayItemStrings(itemStrings);

    }

    public void displayCategory(String category) {

        database.openConn();
        ArrayList<String> itemStrings = database.queryAllItemsByCategory(category);
        database.closeConn();

        displayItemStrings(itemStrings);

    }

    public void displayDrinks(ActionEvent event) {
        displayCategory("Drinks");
    }

    public void displayChocolate(ActionEvent event) {
        displayCategory("Chocolate");
    }

    public void displayCandies(ActionEvent event) {
        displayCategory("Candies");
    }

    public void displayChips(ActionEvent event) {
        displayCategory("Chips");
    }

    public void updateCart(){

        VBox items = new VBox();
        items.setSpacing(30);
        cartScrollPane.setContent(items);

        session.getTransaction().calculateTotal();
        totalLabel.setText("Total: $" + df.format(session.getTransaction().getTotal()));

        for (Map.Entry<String,Integer> entry : session.getTransaction().getItems().entrySet()) {

            HBox item = new HBox();
            item.setPadding(new Insets(10));
            HBox.setMargin(item, new Insets(20));
            item.setPrefSize(250, 50);
            item.setStyle("-fx-background-color:#98aded");

            database.openConn();
            String itemName = database.queryItemName(entry.getKey());
            database.closeConn();
            Label label = new Label(itemName);
            label.setFont(font);

            Region region1 = new Region();
            HBox.setHgrow(region1, Priority.ALWAYS);

            Region region2 = new Region();
            HBox.setHgrow(region2, Priority.ALWAYS);

            Region region3 = new Region();
            HBox.setHgrow(region3, Priority.ALWAYS);

            Label quantityLabel = new Label("" + entry.getValue());
            quantityLabel.setFont(font);

            Button removeButton = new Button("-");
            removeButton.setFont(font);
            removeButton.setOnAction(event -> {
                session.getTransaction().removeItem(entry.getKey());
                quantityLabel.setText("" + session.getTransaction().getItems().get(entry.getKey()));
                if (session.getTransaction().getItems().get(entry.getKey()) == null) {
                    items.getChildren().remove(item);
                }
                session.getTransaction().calculateTotal();
                totalLabel.setText("Total: $" + df.format(session.getTransaction().getTotal()));
            });

            Button addButton = new Button("+");
            addButton.setFont(font);
            addButton.setOnAction(event -> {
                session.getTransaction().addItem(entry.getKey());
                quantityLabel.setText("" + session.getTransaction().getItems().get(entry.getKey()));
                session.getTransaction().calculateTotal();
                totalLabel.setText("Total: $" + df.format(session.getTransaction().getTotal()));
            });

            HBox cartButtons = new HBox();
            cartButtons.setPrefSize(90, 50);
            cartButtons.getChildren().addAll(removeButton, region2, quantityLabel, region3, addButton);

            item.getChildren().addAll(label, region1, cartButtons);
            items.getChildren().add(item);

        }

    }


}

