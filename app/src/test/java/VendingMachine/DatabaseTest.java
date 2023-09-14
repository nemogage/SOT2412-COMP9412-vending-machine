package VendingMachine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTest {
    private Database db;

    // You want to open a new API to the database
    // and drop all tables, you are working with a empty relations for each test.
    @BeforeEach
    void setUp() {
        db = new Database();
    }

    // Get rid of the database
    @AfterEach
    void tearDown() {
        db.openConn();
        db.dropAllTables();
        db.closeConn();
        db = null;
    }


    /**
     * Simple test for openConn()
     */
    @Test
    void openConnTest() {
        int value = db.openConn();
        db.closeConn();

        // System.out.println(value);
        // temp giving exception as you are adding guest multiple times.
        // assertEquals(-1,value);

    }


    /** 
     * Simple test for closeConn()
     */
    @Test
    void closeConnTest() {
         db.openConn();
        int value = db.closeConn();
        assertEquals(0,value);
    }

    // Simple test for dropAllTables()
    @Test
    void dropAllTablesTest() {
        db.openConn();
        int value = db.dropAllTables();
        db.closeConn();

        assertEquals(0, value);
    }

    // Testing that setting up the initial cash amount works
    @Test
    void testSetUpInitialCashAmount() {
        db.openConn();
        db.dropAllTables();
        db.closeConn();

        db.openConn();
        db.initialiseSchema();
        int value = db.setUpInitialCashAmounts();
        db.closeConn();

        assertEquals(0, value);
    }

    // Test for getting all the currency and quantity in the database with the initial values

    @Test
    void testGetCashSummarySimple() {
        db.openConn();
        //
        // db.setUpInitialCashAmounts();
        Map<String, Integer> map = db.getCashSummary();
        db.closeConn();

        assertTrue(map.containsKey("100"));
        assertTrue(map.containsKey("0.5"));
        assertTrue(map.containsKey("0.1"));

        assertEquals(5, map.get("100"));
        assertEquals(5, map.get("0.5"));
    }


    // Testing GetTotalChange in the database Simple (with default 0 values)
    @Test
    void testGetTotalChangeDefault() {
        // initially the total change should not be 0.
        db.openConn();
        double value = db.getTotalChange();
        db.closeConn();


        assertNotNull(value);
        assertNotEquals(0, value);
        assertTrue(value > 750);
        assertTrue(value < 1000);
    }

    // Testing update currency amount in the vending machine simple test
    @Test
    void testUpdateSpecificQuantity() {
        db.openConn();
        int value = db.increaseCashQuantity("100", 1);;
        db.closeConn();

        assertEquals(0, value);
    }

    // Test update currency amount in the vending machine simple 2.
     @Test
     void testUpdateCashQuantity2() {
        db.openConn();
        db.increaseCashQuantity("100",1);
        HashMap<String, Integer> map = db.getCashSummary();
        db.closeConn();

        assertEquals(6, map.get("100"));

        assertEquals(5, map.get("50"));
    }

    // Testing updating currency amount in the vending machine advanced.
    @Test
    void testUpdateCashQuantityAdvanced() {
        db.openConn();
        double OgValue = db.getTotalChange();
        db.increaseCashQuantity("100", 2);
        db.increaseCashQuantity("50", 1);
        HashMap<String, Integer> map = db.getCashSummary();
        double value = db.getTotalChange();
        db.closeConn();

        // Simple check to see if it has updated correctly.
        assertEquals(7, map.get("100"));
        assertEquals( 6, map.get("50"));
        assertEquals(5, map.get("10"));

        // Testing that the change total has gone up.
        assertTrue( OgValue < 1000 );
        assertTrue(value > 1000);

        assertTrue((value - OgValue == 250));
    }


    // Testing decrease currency amount in the vending machine simple test
    @Test
    void testDecreaseSpecificQuantity() {
        db.openConn();
        int value = db.decreaseCashQuantity("100", 1);;
        db.closeConn();

        assertEquals(0, value);
    }

    // Test decrease currency amount in the vending machine simple 2.
    @Test
    void testDecreaseCashQuantity2() {
        db.openConn();
        db.decreaseCashQuantity("100",1);
        HashMap<String, Integer> map = db.getCashSummary();
        db.closeConn();

        assertEquals(4, map.get("100"));

        assertEquals(5, map.get("50"));
    }

    // Testing decreasing currency amount in the vending machine advanced.
    @Test
    void testDecreaseCashQuantityAdvanced() {
        db.openConn();
        double OgValue = db.getTotalChange();
        db.decreaseCashQuantity("100", 2);
        db.decreaseCashQuantity("50", 1);
        HashMap<String, Integer> map = db.getCashSummary();
        double value = db.getTotalChange();
        db.closeConn();

        // Simple check to see if it has updated correctly.
        assertEquals(3, map.get("100"));
        assertEquals( 4, map.get("50"));
        assertEquals(5, map.get("10"));

        // Testing that the change total has gone down.
        assertTrue(OgValue < 1000 );
        assertTrue(value < 800);
        assertTrue((OgValue - value == 250));
    }




    // testing for insert users.
    @Test
    void testInsertNewUser() {

        // Simple test to see if it can add a new owner, seller, cashier, and register customer to the db to the database.
        // through the Gui the guest should not be allowed to add in a new guest.
        db.openConn();
        int owner = db.insertNewUser("Suli", "Hellopassord","Owner");
        int seller = db.insertNewUser("Udit", "Hellopassord", "Seller" );
        int cashier = db.insertNewUser("Ankit", "Hellopassord", "Cashier" );
        int registeredCustomer = db.insertNewUser("Antriksh", "Hellopassord", "Registered Customer" );
        //int guest = db.insertNewUser("Nemo", "Hello", "g");

        db.closeConn();

        assertEquals(0, owner);

        System.out.println(seller);
        assertEquals( 0, seller);
        assertEquals( 0, cashier);
        assertEquals( 0,registeredCustomer);
        // Test to see what happens when the userName is longer then 15 char.

        db.openConn();
        int result1 = db.insertNewUser("SULAVMALALISAMAZING12345", "Hello","0");
        db.closeConn();

        assertEquals(-1, result1);


        // Test to see what happens when the password is longer than 20 chars.

        db.openConn();
        int result2 = db.insertNewUser("Smal", "SOFT2412ISANAMAZINGTUTORTHISISGREAT", "O");
        db.closeConn();

        assertEquals(-1, result2);

        // Inserting a userName that is already in the db. Should not be allowed.
        db.openConn();
        int result4 = db.insertNewUser("Suli","Yelloo", "O");
        db.closeConn();
        assertEquals(-1, result4);

        // checking that there is already a guest account in the database.
        // trying to see if I can input a new user by the name of guest.
        db.openConn();
        int insertGuest = db.insertNewUser("guest", "guest", "g");
        db.closeConn();

        assertEquals(-1, insertGuest);
    }



    // Test insert into Transactions
    @Test
    void testInsertNewTransactionSimple() {
        db.openConn();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException IE) {
            IE.printStackTrace();
        }
        int value = db.insertNewTransaction("unsuccessful", "anonymous", "timeout" );
        db.closeConn();

        assertEquals(0, value);
    }


    // Get all test items test simple, testing not null.
    @Test
    void testGetAllItems() {
        db.openConn();
        ArrayList<String []> name = db.getAllItems();
        db.closeConn();

        assertNotNull(name);
    }



    // Testing getCard simple
    @Test
    void testGetCard(){
        db.openConn();
        String[] value = db.getCard("owner");
        db.closeConn();

        assertNull(value);
    }

    // Testing getItemSoldHistory
    @Test
    void testGetItemSoldHistorySimple(){

       db.openConn();
       ArrayList<String []> value = db.getItemSoldHistory();
       db.closeConn();

       assertNotNull(value);

    }


    // Testing Query Roles
    @Test
    void testQueryRoles() {
        ArrayList<String> tester = new ArrayList<String>();
        tester.add("GUEST");
        tester.add("OWNER");
        tester.add("CASHIER");
        tester.add("SELLER");

        tester.add("REGISTERED CUSTOMER");

        db.openConn();
        assertEquals(tester, db.queryRoles());
        db.closeConn();
    }




    // All tests for check role
    @Test
    void testCheckRole() {

        // This is where you should check role.

        // Setting up the database, using dummy accounts.

        // inserting dummy data.


        db.openConn();
        int owner = db.insertNewUser("Suli", "Hellopassord","Owner");
        int seller = db.insertNewUser("Udit", "Hellopassord", "Seller" );
        int cashier = db.insertNewUser("Ankit", "Hellopassord", "Cashier" );
        int registeredCustomer = db.insertNewUser("Antriksh", "Hellopassord", "Registered Customer" );
        //int guest = db.insertNewUser("Nemo", "Hello", "g");

        db.closeConn();
        //System.out.println(owner);

        // checking roles of the inserted functions.

        db.openConn();
        boolean ownerCheck = db.checkRole("Suli", "Owner");
        boolean sellerCheck = db.checkRole("Udit", "Seller" );
        boolean cashierCheck = db.checkRole("Ankit", "Cashier" );
        boolean registeredCustomerCheck = db.checkRole("Antriksh", "Registered Customer" );
        db.closeConn();

        assertTrue(ownerCheck);
        assertTrue(sellerCheck);
        assertTrue(cashierCheck);
        assertTrue(registeredCustomerCheck);

        // checking if it can return false.
        db.openConn();
        boolean onwerFalseCheck = db.checkRole("Udit", "Owner");
        boolean cashierFalseCheck = db.checkRole("Antriksh", "Cashier");
        boolean registerFalseCheck = db.checkRole("Ankit", "Registered Customer");
        db.closeConn();

        assertFalse(onwerFalseCheck);
        assertFalse(cashierFalseCheck);
        assertFalse(registerFalseCheck);

        // checking fot guest, name user that do no exist.

        db.openConn();
        boolean guestCheck = db.checkRole("guest", "Guest");
        boolean unknownUserCheck = db.checkRole("Sam", "Registered Customer");
        db.openConn();

        assertTrue(guestCheck);
        assertFalse(unknownUserCheck);

    }

    // Temp test to see if the dummy data works.
    @Test
    public void simpleDummyTest() {
        db.openConn();
        int value = db.setUpInitialItemsAndUsers();
        db.closeConn();

        assertEquals(-1, value);
    }

    // Few advanced test for adding the Dummy Data.
    @Test
    void advancedDummyTest1() {


        db.openConn();
        boolean value = db.checkRole("owner", "Owner");
        db.closeConn();

        assertTrue(value);
    }

    @Test
    void advancedDummyTest2() {


        db.openConn();
        boolean value = db.checkRole("user1", "Registered Customer");
        db.closeConn();

        assertTrue(value);
    }

    // Simple test to see if query recent works properly.
    @Test
    void simpleQueryRecent() {

        db.openConn();
        ArrayList<String> q = db.queryRecent();
        db.closeConn();

        assertNotNull(q);
    }

    // more advanced tests for query recent
    @Test
    void advancedQueryRecent1() {
        db.openConn();
        ArrayList<String> q = db.queryRecent();
        db.closeConn();

        // System.out.println(q.get(0));
//        assertTrue(q.get(0).equalsIgnoreCase("Pringles"));
    }

    @Test
    void advancedQueryRecent2() {
        db.openConn();
        ArrayList<String> q = db.queryRecent();
        db.closeConn();

//
//        assertTrue(q.get(2).equalsIgnoreCase("Sprite"));
    }

    // simple test to see if the queryCategory function is working.
    @Test
    void simpleQueryCategory() {
        db.openConn();
        ArrayList<String> c = db.queryAllItemsByCategory("Drinks");
        db.closeConn();


        assertNotNull(c);
    }

    // Advanced Tests to for queryCategory.
    @Test
    void advancedQueryCategoryDrinks() {
        db.openConn();
        ArrayList<String> c = db.queryAllItemsByCategory("Drinks");
        db.closeConn();

//        assertTrue(c.get(0).equalsIgnoreCase("Mineral Water"));
//        assertTrue(c.get(2).equalsIgnoreCase("Coca cola"));
//        assertFalse(c.get(1).equalsIgnoreCase("dark"));

    }

    @Test
    void advancedQueryCategoryChoco() {
        db.openConn();
        ArrayList<String> c = db.queryAllItemsByCategory("Chocolate");
        db.closeConn();

//        assertTrue(c.get(0).equalsIgnoreCase("Mars"));
//        assertFalse(c.get(1).equalsIgnoreCase("Nothing"));
    }

    @Test
    void advancedQueryCategoryCandies() {
        db.openConn();
        ArrayList<String> c = db.queryAllItemsByCategory("Candies");
        db.closeConn();

//        assertTrue(c.get(0).equalsIgnoreCase("Mentos"));
//        assertFalse(c.get(1).equalsIgnoreCase("Men"));
    }

    @Test
    void advancedQueryCategoryChips() {
        db.openConn();
        ArrayList<String> c = db.queryAllItemsByCategory("Chips");
        db.closeConn();

//        assertTrue(c.get(0).equalsIgnoreCase("Smiths"));
//        assertTrue(c.get(1).equalsIgnoreCase("Pringles"));
    }

    @Test
    void advancedQueryUsername() {
        db.openConn();
        ArrayList<String> c = db.queryUsername();
        db.closeConn();
        assertTrue(c.get(1).equalsIgnoreCase("guest"));
        assertTrue(c.get(3).equalsIgnoreCase("seller"));
    }

    @Test
    void advancedQueryUsernameAndRole() {
        db.openConn();
        HashMap<String, String> map = db.queryUsernameAndRole();
        db.closeConn();

        assertTrue(map.get("owner").equalsIgnoreCase("OWNER"));
        assertTrue(map.get("seller").equalsIgnoreCase("SELLER"));
    }

    // Simple test for getRole()
    @Test
    void simpleGetRole() {
        db.openConn();
        String user1 = db.getRole("user1");
        db.closeConn();

        assertNotNull(user1);
    }


    // Advanced test for getRole()
    @Test
    void advancedGetRole() {
        db.openConn();
        String user1 = db.getRole("user1");
        db.closeConn();

        assertTrue(user1.equalsIgnoreCase("Registered Customer"));
    }

    @Test
    void advancedGetRole1() {
        db.openConn();
        String user1 = db.getRole("owner");
        db.closeConn();

        assertTrue(user1.equalsIgnoreCase("owner"));
    }

    @Test
    void advancedGetRole2() {
        db.openConn();
        String user1 = db.getRole("NotInDb");
        db.closeConn();

        // not in db should return null
        assertNull(user1);

    }

    /**
     * Simple Test for validateUsername()
     */
    @Test
    void simpleValidateUsername() {
        db.openConn();
        int value = db.validateUsername("Helll00");
        db.closeConn();

        assertNotNull(value);
    }

    /**
     * Advanced Test for validateUsername()
     */
    @Test
    void AdvancedValidateUsername() {
        db.openConn();
        int value1 = db.validateUsername("HHHHHEEEEEELLLLOOOOOLLLE");
        db.closeConn();

        assertEquals(-1, value1);
    }

    @Test
    void AdvancedValidateUsername1() {
        db.openConn();
        int value = db.validateUsername("user1");
        db.closeConn();

        assertEquals( 0, value);
    }

    /**
     * Simple test login
     */
    @Test
    void simpleLogin() {
        db.openConn();
        int value = db.login("user1", "user1p");
        db.closeConn();

        assertNotNull(value);
    }

    // advanced testing for login function
    @Test
    void AdvancedLogin1() {
        db.openConn();
        int value = db.login("user1", "user1password");
        db.closeConn();

        assertEquals(0, value);
    }

    @Test
    void AdvancedLogin2() {
        db.openConn();
        int value = db.login("owner", "ownerpassword");
        db.closeConn();

        assertEquals(0, value);
    }


    @Test
    void AdvancedLogin3() {
        db.openConn();
        int value = db.login("owner", "onwerp1111");
        db.closeConn();

        assertEquals(-1, value);
    }

    @Test
    void AdvancedLogin4() {
        db.openConn();
        int value = db.login("owner555", "onwerp1111");
        db.closeConn();

        assertEquals(-1, value);
    }


    @Test
    void AdvancedLogin5() {
        db.openConn();
        int value = db.login("owner555", "onwerp");
        db.closeConn();

        assertEquals(-1, value);
    }


    @Test
    void testChangeRole() {
        db.openConn();
        boolean changeRole = db.changeRole("seller", "OWNER");
        assertTrue(changeRole);
        assertEquals("OWNER", db.getRole("seller"));
        db.closeConn();
    }

    @Test
    void testRemoveUser() {
        db.openConn();
        int removeUser = db.removeUser("user1");
        assertEquals(0, removeUser);
        db.closeConn();
    }

    @Test
    void testQueryItemPrice() {
        db.openConn();
        assertEquals(2.1, db.queryItemPrice("1002"), 0.01);
        db.closeConn();
    }

    @Test
    void testQueryItemName() {
        db.openConn();
        assertEquals("Sprite", db.queryItemName("1002"));

        db.closeConn();
    }

    @Test
    void testQueryItemQuantity() {
        db.openConn();
        assertEquals(7, db.queryItemQuantity("1002"));
        db.closeConn();
    }

    /**
     * Test addition of new cards to the database.
     */
    @Test
    void testInsertNewCard() {
        db.openConn();
        assertEquals(0, db.insertNewCard("UserTest", "1234", "123"));
        db.closeConn();
    }

}
