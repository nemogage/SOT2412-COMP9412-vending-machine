package VendingMachine;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transaction {

    private SceneManager sceneManager;

    private HashMap<String, Integer> items;

    // Values that need to be tracked.
    private double total;

    private double paid;

    private double due;

    private double change;


    private ArrayList<String> changeOrder = new ArrayList<>();

    private HashMap<String, Integer> currentlyPaid = new HashMap<>();

    // Adding doubleProperty for dynamic text on screen.
    // Note you do not need a doubleProperty for the total as it will never change on screen.
    // You also never need it for paid as the paid amount will never appear on screen.
    private DoubleProperty changeAmount = new SimpleDoubleProperty() ;

    private DoubleProperty totalAmount = new SimpleDoubleProperty();
    private DoubleProperty dueAmount = new SimpleDoubleProperty();


    /**
     * Constructor for transaction class
     */
    public Transaction() {
        this.reset();
        this.initialHashMap();
        changeOrder.add("100");
        changeOrder.add("50");
        changeOrder.add("20");
        changeOrder.add("10");
        changeOrder.add("5");
        changeOrder.add("2");
        changeOrder.add("1");
        changeOrder.add("0.5");
        changeOrder.add("0.2");
        changeOrder.add("0.1");
        changeOrder.add("0.05");
    }


    /**
     * Function to reset all the amounts to 0
     */
    public void reset() {
        total = 0;
        due = 0;
        change = 0;
        paid = 0;
        changeAmount.set(change);
        dueAmount.set(due);
        totalAmount.set(total);
        items = new HashMap<>();
    }


    /**
     * Function for adding an item to the cart
     * @param item
     * @return
     */
    public boolean addItem(String item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
        }
        else {
            items.put(item, 1);
        }
        return true;
    }


    /**
     * Function for increasing the total price
     * @param n
     */

    public void addToTotal(double n) {
        total += n;
        calculateDue();
        calculateChange();
        totalAmount.set(total);

    }


    /**
     * Function that gets the total price of all the items in the cart.
     * @return total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Function that return the HashMap with the number of items.
     * @return items
     */
    public HashMap<String, Integer> getItems() {
        return items;
    }

    /**
     * Function that calculateTotal
     */
    public void calculateTotal() {

        total = 0;

        for (Map.Entry<String,Integer> entry : items.entrySet()) {
            sceneManager.getDatabase().openConn();
            double price = sceneManager.getDatabase().queryItemPrice(entry.getKey());
            sceneManager.getDatabase().closeConn();

            total += price * entry.getValue();
        }

        calculateDue();
        calculateChange();
        totalAmount.set(total);

    }






    /**
     * Function that return the changes the transaction needs to return.
     * @return change
     */
    public double getChange() {
        return change;
    }


    /**
     * Function that return the amount still due to complete transaction.
     * @return due
     */
    public double getDue() {
        return due;
    }


    /**
     * Function that removes an item in the cart.
     * @param item
     */
    public void removeItem(String item) {
        if (items.containsKey(item)) {
            int n = items.get(item) - 1;
            if (n <= 0) {
                items.remove(item);
            } else {
                items.put(item, n);
            }
        }
    }


    /**
     * Function that recalculates the change that should be returned.
     */
    void calculateChange() {
        this.change = paid - total;
        if (this.change < 0) {
            this.change = 0;
        }
        changeAmount.set(change);
    }


    /**
     * Function that recalculates the due amount for the transaction.
     */
    void calculateDue() {
        this.due = total - paid;
        if (due < 0)
            due = 0;

        dueAmount.set(due);
    }


    /**
     * Function that returns the change order in an ArrayList
     * @return changeOrder
     */
    public ArrayList<String> getChangeOrder() {
        return changeOrder;
    }

    
    /**
     * Function that sets the amount paid and recalculates due / changes.
     * @param paid
     */
    public void setPaid(double paid) {
        this.paid = paid;
        calculateChange();
        calculateDue();
    }


    /**
     * Function that return the total that has been paid so far.
     * @return paid
     */
    public double getPaid() {
        return this.paid;
    }


    /**
     * Function that return the change doubleProperty
     * @return changeAMount
     */
    public DoubleProperty getChangeAmount() {
        return this.changeAmount;
    }


    /**
     * Function that return the Due doubleProperty.
     * @return dueamount
     */
    public DoubleProperty getDueAmount() {
        return this.dueAmount;
    }


    public DoubleProperty getTotalAmount(){
        return this.totalAmount;
    }

    /**
     * Function for putting the initial setting up the hashmap.
     */
    public void initialHashMap() {

        currentlyPaid.put("100", 0);
        currentlyPaid.put("50",0);
        currentlyPaid.put("20",0);
        currentlyPaid.put("10",0);
        currentlyPaid.put("5",0);
        currentlyPaid.put("2",0);
        currentlyPaid.put("1",0);
        currentlyPaid.put("0.5",0);
        currentlyPaid.put("0.2",0);
        currentlyPaid.put("0.1",0);
        currentlyPaid.put("0.05",0);

    }


    /**
     *  Function that updates the quantity for the givens quantity for a currency by 1.
     * @param key (currency you want to update by 1) : String
     */
    public void addToCurrencyPaid(String key) {

        // You can only update keys that are already in the hashmap
        if (!currentlyPaid.containsKey(key)) {
            return;
        }

        currentlyPaid.put(key, currentlyPaid.get(key) + 1);
    }


    /**
     * Function that return the currently paid hashMap.
     * @return currentlyPaid
     */
    public HashMap<String, Integer> getCurrentlyPaid() {
        return this.currentlyPaid;
    }


    /**
     * Function that returns the quantity paid so far for a given currency in currently paid.
     * @param key
     * @return
     */
    public int getQuantityPaid(String key) {
        return currentlyPaid.get(key);
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
