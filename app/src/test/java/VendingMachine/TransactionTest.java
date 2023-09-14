package VendingMachine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionTest {
    private Transaction t;

    // You want to open a new API to the database
    // and drop all tables, you are working with a empty relations for each test.
    @BeforeEach
    void setUp(){
        t = new Transaction();
    }


    // Get rid of the database
    @AfterEach
    void tearDown(){
        t = null;
    }


    // simple test for testing inital total = 0
    @Test
    void testInitialTotal() {
        assertEquals(t.getTotal(), 0.0);
    }

    @Test
    void testAddToTotal() {
        t.addToTotal(12);
        assertEquals(t.getTotal(), 12.0);
    }

    @Test
    void testGetItems() {
        assertNull(t.getItems().get("Sprite"));
        t.addItem("Sprite");
        assertEquals(t.getItems().get("Sprite"), 1);
        t.addItem("Sprite");
        assertEquals(t.getItems().get("Sprite"), 2);
        t.addItem("Sprite");
        assertEquals(t.getItems().get("Sprite"), 3);

    }

    @Test
    void testRemoveItem() {
        t.addItem("Sprite");
        t.addItem("Sprite");
        t.addItem("Sprite");
        t.removeItem("Sprite");
        assertEquals(t.getItems().get("Sprite"), 2);
        t.removeItem("Sprite");
        assertEquals(t.getItems().get("Sprite"), 1);
        t.removeItem("Sprite");
        assertNull(t.getItems().get("Sprite"));

    }

    // Testing if change is initially is 0.
    @Test
    void testGetChangeInitial(){
        double change = t.getChange();

        assertEquals(0, change);
    }

    // Testing if due amount is initially is 0;\
    @Test
    void testGetDueInitial(){
        double due = t.getDue();

        assertEquals(0, due);
    }

    // Testing that Paid is 0 initially
    @Test
    void testGetPaid(){
        double paid = t.getPaid();

        assertEquals(0, paid);
    }

    // Testing setting the paid amount initial.
    @Test
    void testSetPaid(){
        t.setPaid(20);
        double paid = t.getPaid();

        assertEquals(20, paid);
    }

    //Testing set paid amount advanced
    @Test
    void testSetPaidAdvanced(){
        t.setPaid(20);
        t.setPaid(30);

        double paid1 = t.getPaid();
        t.setPaid(40);

        double paid2 = t.getPaid();

        assertEquals(30, paid1);
        assertEquals(40,paid2);
        assertNotEquals(40, paid1);
    }


    // Testing Calculate change and due simple
    @Test
    void testCalculateChangDue(){
        // the total price is 0 so anything that is paid should be returned as change,
        // and the due amount should till be 0.
        t.setPaid(20);
        double change = t.getChange();
        double due = t.getDue();


        assertEquals(20, change);
        assertEquals(0,due);
    }


   // Testing teh getter for the change order list
   @Test
   void testGetChangeOrder(){
        ArrayList<String> changeOder = t.getChangeOrder();

        assertNotNull(changeOder);
        assertTrue(changeOder.get(0).equalsIgnoreCase("100"));
        assertTrue(changeOder.get(2).equalsIgnoreCase("20"));
   }


    //Testing Calculate change and due advanced
    @Test
    void testCalculateChangeDueAdvanced(){

        // Setting the total to 20
        t.addToTotal(20);

        // Has only paid 10
        t.setPaid(10);
        double change1 = t.getChange();
        double due1 = t.getDue();

        assertEquals(0, change1);
        assertEquals(10, due1);

        //Has paid 20
        t.setPaid(20);
        double change2 = t.getChange();
        double due2 = t.getDue();

        assertEquals(0, change2);
        assertEquals(0, due2);

        // Has paid 30
        t.setPaid( 30);
        double change3 = t.getChange();
        double due3 = t.getDue();

        assertEquals( 10, change3);
        assertEquals(0, due3);


        // Setting new total to 30.55
        t.addToTotal(10.55);
        t.setPaid(30);
        double total1 = t.getTotal();
        double change4 = t.getChange();
        double due4 = t.getDue();

        assertEquals(30.55 ,total1);
        assertEquals(30, t.getPaid());
        assertEquals(0, change4);
        assertFalse(due4 == 0);

        // Setting paid to 31
        t.setPaid(31);
        double change5 = t.getChange();
        double due5 = t.getDue();

        assertFalse(change5 == 0 );
        assertEquals(0, due5);

        // Resetting all the figures
        t.reset();
        double resetPaid = t.getPaid();
        double resetDue = t.getDue();
        double resetTotal = t.getTotal();
        double resetChange = t.getChange();

        assertEquals(0, resetChange);
        assertEquals(0, resetDue);
        assertEquals(0, resetPaid);
        assertEquals(0, resetTotal);
    }

    // Testing reset simple
    @Test
    void testReset(){
        t.addToTotal(200);
        t.setPaid(30);

        t.reset();

        double total = t.getTotal();
        double paid = t.getPaid();
        double change = t.getChange();
        double  due = t.getDue();

        assertEquals(0, total);
        assertEquals(0, paid);
        assertEquals(0, change);
        assertEquals(0, due);
    }

    // Testing the Currently paid hashmap is not null
   @Test
    void testNotNullCurrentlyPaid(){

        assertNotNull(t.getCurrentlyPaid());
    }

    // Testing the value in currentlyPaid with the default values.
    @Test
    void testCurrentlyPaid(){

        int value1 = t.getQuantityPaid("100");
        int value2 = t.getQuantityPaid("0.05");

        assertEquals(0, value1);
        assertEquals( 0,value2);

        assertTrue(t.getCurrentlyPaid().containsKey("50"));
        assertTrue(t.getCurrentlyPaid().containsKey("0.1"));


    }

    // Testing increasing the value in added currentlyPaid
    @Test
    void testAddToCurrencyPaid() {
        t.addToCurrencyPaid("100");
        t.addToCurrencyPaid("100");
        t.addToCurrencyPaid("50");
        t.addToCurrencyPaid("0.5");
        t.addToCurrencyPaid("22");

        assertEquals(2, t.getQuantityPaid("100"));
        assertEquals(1, t.getQuantityPaid("50"));
        assertEquals(0,t.getQuantityPaid("0.2"));
        assertEquals(1,t.getQuantityPaid("0.5"));
    }
}
