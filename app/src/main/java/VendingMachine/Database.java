package VendingMachine;

import java.sql.*;
import java.util.*;

//////////////////////// README ////////////////////////
/**
 * This is a basic instruction manual for this Vending-
 * Machine.
 * 
 * Everytime you would like to access any of these functions, 
 * please openConn(), then after you are done
 * closeConn().
 * 
 * Each function has comments explaining all of the
 * parameters and what the function does.
 */
///////////////////////////////////////////////////////

public class Database {

    private Connection dbConn = null;
    private Statement openStatement = null;

    /**
     * Constructor for this class.
     */
    public Database() {
        System.out.println("Attempting to connect to the database for the first time...");
        int successfulConn = openConn();
        System.out.println();
        if (successfulConn == 0) {
            dropAllTables();
            this.initialiseSchema();
            this.setUpInitialItemsAndUsers();
            this.setUpInitialCashAmounts();
            this.closeConn();
        }
    }


    /**
     * Sets up the database with all tables.
     * See UML diagram for schema details.
     * 
     * Note: this function is only to be called
     * if openConn() is successful.
     * 
     * @return 0 on success, -1 on error
     */
    public int initialiseSchema() {
        try {
            openStatement = dbConn.createStatement();
            openStatement.setQueryTimeout(30); // set timeout to 30 sec.
            
            openStatement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS users (
                        username VARCHAR(15) PRIMARY KEY, 
                        password VARCHAR(50),
                        salt VARCHAR(50), 
                        role VARCHAR(20),
                        CHECK (role IN ('OWNER', 'SELLER', 'CASHIER', 'GUEST', 'REGISTERED CUSTOMER'))
                    );
                    """);

            openStatement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS categories (
                        category_id SERIAL PRIMARY KEY,
                        category_name VARCHAR(20)
                    );
                    """);

            openStatement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS items (
                        item_code VARCHAR(20) PRIMARY KEY,
                        item_name VARCHAR(20),
                        category_name VARCHAR(20),
                        quantity int,
                        price double,
                        quantity_sold int
                    );
                    """);

            openStatement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS recent (
                        item_code VARCHAR(20) PRIMARY KEY
                    )
                    """);

            openStatement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS cash (
                        currency VARCHAR(4) PRIMARY KEY,
                        quantity INTEGER,
                        CHECK (currency IN ('100', '50', '20', '10', '5', '2', '1', '0.5', '0.2', '0.1', '0.05'))
                    );
                    """);

            openStatement.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS cards (
                    username VARCHAR(20) PRIMARY KEY,
                    card VARCHAR(16),
                    cvv VARCHAR(3)
                )
                """);

            openStatement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS cards (
                            username VARCHAR(20) PRIMARY KEY,
                            card VARCHAR(16),
                            cvv VARCHAR(3)
                        )
                            """);

            openStatement.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS transactions(
                            time_added DATETIME DEFAULT (CURRENT_TIMESTAMP) PRIMARY KEY,
                            status VARCHAR(16), -- Successful or Unsuccessful 
                            users VARCHAR(20), -- who attempted the transaction, if guest should be anonymous 
                            reason VARCHAR(50) -- there should only be a reasons only if the transaction has been cancelled. ) 
                            )
                            """);


            // The two lines below are commented out as they have already been "done"
            // Initialise db with a guest account

            // OWNER - O
            // CASHIER - C
            // GUEST - G
            // REGISTERED CUSTOMER - R
            
            } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        } return 0;
    }


    /**
     * Opens connection with the database.
     * To be called before any other function by calling class.
     *
     * @return 0 if successful and -1 if unsuccessful
     */
    public int openConn() {
        try {
            // create a database connection
            dbConn = DriverManager.getConnection("jdbc:sqlite:vending_machine.db");
            openStatement = dbConn.createStatement();
            openStatement.setQueryTimeout(30); // set timeout to 30 sec.
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }

        return 0;
    }


    /**
     * Closes the connection with the database.
     * To be called after you are done with database.
     *
     * @return 0 if successful and -1 if unsuccessful
     */
    public int closeConn() {
        try {
            if (dbConn != null) {
                dbConn.close();
            }

            this.dbConn = null;
            this.openStatement = null;
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
            // this.dbConn = null;
            return -1;
        }
        return 0;
    }


    /**
     * Deletes all table data from the database (that we will be using).
     * To only be called if you want to delete all data.
     *
     * @return 0 if successful and -1 if unsuccessful
     */
    public int dropAllTables() {
        try {
            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(
                """
                DROP TABLE IF EXISTS users; 
                DROP TABLE IF EXISTS categories;
                DROP TABLE IF EXISTS items;
                DROP TABLE IF EXISTS recent;
                DROP TABLE IF EXISTS cash;  
                DROP TABLE IF EXISTS cards;
                DROP TABLE IF EXISTS transactions;
                """);
            return 0;
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }
    }


    /**
     * FOR TESTING PURPOSES ONLY:
     * Add a bit of dummy data to test the GUI during demonstrations.
     * @return
     */
    public int setUpInitialItemsAndUsers() {
        try {
            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(String.format("insert into items values('1001', 'Mineral Water', 'Drinks', 7, 1.5, 0);"));
            statement.executeUpdate(String.format("insert into items values('1002', 'Sprite', 'Drinks', 7, 2.1, 0);"));
            statement.executeUpdate(String.format("insert into items values('1003', 'Coca cola', 'Drinks', 7, 2.1, 0);"));
            statement.executeUpdate(String.format("insert into items values('1004', 'Pepsi', 'Drinks', 7, 2.1, 0);"));
            statement.executeUpdate(String.format("insert into items values('1005', 'Juice', 'Drinks', 7, 5.3, 0);"));
            statement.executeUpdate(String.format("insert into items values('2001', 'Mars', 'Chocolate', 7, 1, 0);"));
            statement.executeUpdate(String.format("insert into items values('2002', 'M&M', 'Chocolate', 7, 3.5, 0);"));
            statement.executeUpdate(String.format("insert into items values('2003', 'Bounty', 'Chocolate', 7, 1, 0);"));
            statement.executeUpdate(String.format("insert into items values('2004', 'Snickers', 'Chocolate', 7, 1, 0);"));
            statement.executeUpdate(String.format("insert into items values('3001', 'Smiths', 'Chips', 7, 4.3, 0);"));
            statement.executeUpdate(String.format("insert into items values('3002', 'Pringles', 'Chips', 7, 4, 0);"));
            statement.executeUpdate(String.format("insert into items values('3003', 'Kettle', 'Chips', 7, 4.5, 0);"));
            statement.executeUpdate(String.format("insert into items values('3004', 'Thins', 'Chips', 7, 2.15, 0);"));
            statement.executeUpdate(String.format("insert into items values('4001', 'Mentos', 'Candies', 7, 1.5, 0);"));
            statement.executeUpdate(String.format("insert into items values('4002', 'Sour Patches', 'Candies', 7, 3, 0);"));
            statement.executeUpdate(String.format("insert into items values('4003', 'Skittles', 'Candies', 7, 3.8, 0);"));

            statement.executeUpdate(String.format("insert into recent values('%s')", "3002"));
            statement.executeUpdate(String.format("insert into recent values('%s')", "1003"));
            statement.executeUpdate(String.format("insert into recent values('%s')", "4002"));

            insertNewUser("guest", "guestpassword", "GUEST");
            insertNewUser("owner", "ownerpassword", "OWNER");
            insertNewUser("cashier", "cashierpassword", "cashier");
            insertNewUser("seller", "sellerpassword", "SELLER");

            insertNewUser("user1", "user1password", "REGISTERED CUSTOMER");
            insertNewUser("user2", "user2password", "REGISTERED CUSTOMER");
            insertNewUser("user3", "user3password", "REGISTERED CUSTOMER");

            System.out.println("Finished setUpInitialItemsAndUsers");

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }
        return 0;
    }

    /**
     * Function that sets up the initial cash amount for the first run.
     * @return if successful return 0, else return -1
     */
    public int setUpInitialCashAmounts() {
        try {
            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "100", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "50", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "20", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "10", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "5", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "2", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "1", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "0.5", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "0.2", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "0.1", 5));
            statement.executeUpdate(String.format("insert into cash values ('%s', %d)", "0.05", 5));
        }
        catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }

        return 0;
    }


    /**
     * Function that returns the currency and quantity for all currencies in a hashmap formatted <currency, quantity>.
     * @return
     */
    public HashMap<String, Integer> getCashSummary() {

        HashMap<String, Integer> items = new HashMap<>();

        try {
            ResultSet query = openStatement.executeQuery(String.format("SELECT * FROM cash"));
            while (query.next()) {
                items.put(query.getString("currency"), query.getInt("quantity"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return items;
    }

    /**
     * Function tha calculates the total change in the vending machine
     * @return total
     */
    public double getTotalChange() {
        HashMap<String, Integer> changeMap = this.getCashSummary();

        double total = 0.00;
        for (Map.Entry<String, Integer> entry : changeMap.entrySet()) {
            total += (Double.parseDouble(entry.getKey()) * entry.getValue());
        }
        return total;
    }

    /**
     * Function to update the number of available change in the vending machine.
     * @param currency ( the currency you want to update)
     * @param quantityToUpdate (quantity you want the cash to update by)
     */
    public int increaseCashQuantity(String currency, Integer quantityToUpdate) {
        HashMap<String,Integer> availableCashMap = this.getCashSummary();

        try {
            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(String.format("update cash set quantity = %d where currency = '%s'", quantityToUpdate + availableCashMap.get(currency), currency));

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }
        return 0;
    }


    /**
     * Function to deduct a quantity of a given currency.
     * @param currency
     * @param quantityToDeduct
     * @return
     */
    public int decreaseCashQuantity(String currency, Integer quantityToDeduct) {
        HashMap<String,Integer> availableCashMap = this.getCashSummary();

        try {
            int newAmount = availableCashMap.get(currency) - quantityToDeduct;
            if (newAmount < 0) {
                throw new SQLException();
            }

            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(String.format("update cash set quantity = %d where currency = '%s'", newAmount, currency));

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }
        return 0;
    }


    /**
     * Function to query recent items in the store.
     * @return items
     */
    public ArrayList<String> queryRecent() {
        
        ArrayList<String> items = new ArrayList<>();
        
        try {
            ResultSet query = openStatement.executeQuery(String.format("SELECT * FROM recent"));
            while (query.next()) {
                items.add(query.getString("item_code"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        
        return items;
    }

    /**
     * Function that enters a new transaction into the transaction table.
     * @param status
     * @param user
     * @param reason
     * @return
     */
    public int insertNewTransaction(String status, String user, String reason) {

        try {
            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(String.format("insert into transactions values(CURRENT_TIMESTAMP ,'%s', '%s', '%s')", status, user, reason));
        } catch(SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }

        return 0;
    }


    /**
     * Function that allows for a category to be queried.
     * @param category
     * @return items
     */
    public ArrayList<String> queryAllItemsByCategory(String category) {

        ArrayList<String> items = new ArrayList<>();

        try {
            String sql = String.format("SELECT item_code FROM items WHERE category_name = '%s'", category);
            ResultSet query = openStatement.executeQuery(sql);
            while (query.next()) {
                items.add(query.getString("item_code"));
            }
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return items;

    }

    /**
     * Function that allows for a item to be queried.
     * @param itemCode
     * @return itemsName
     */
    public String queryItemName(String itemCode) {

        try {
            String sql = String.format("SELECT item_name FROM items WHERE item_code = '%s'", itemCode);
            ResultSet query = openStatement.executeQuery(sql);
            return query.getString("item_name");
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Function that allows for an item to be queried.
     * @param itemCode
     * @return itemPrice
     */
    public Double queryItemPrice(String itemCode) {

        try {
            String sql = String.format("SELECT price FROM items WHERE item_code = '%s'", itemCode);
            ResultSet query = openStatement.executeQuery(sql);
            return query.getDouble("price");
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Function that allows for an item to be queried.
     * @param itemCode
     * @return Quantity
     */
    public int queryItemQuantity(String itemCode) {

        try {
            String sql = String.format("SELECT quantity FROM items WHERE item_code = '%s'", itemCode);
            ResultSet query = openStatement.executeQuery(sql);
            return query.getInt("quantity");
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return -1;
    }


    /**
     * Function that allows for the username and role to be queried.
     * 
     * @return map
     */
    public HashMap<String, String> queryUsernameAndRole() {

        HashMap<String, String> map = new HashMap<>();

        try {
            String sql = String.format("SELECT username, role FROM users");
            ResultSet query = openStatement.executeQuery(sql);
            while (query.next()) {
                map.put(query.getString("username"), query.getString("role"));
            }
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return map;

    }

    /**
     * Function that allows for the cancelled transactions to be queried.
     * @return map
     */
//    public ArrayList<ArrayList<String>> queryCancelledTransactions() {
//
//        ArrayList<ArrayList<String>> table = new ArrayList<>();
//        // System.out.println("Hello queryCancelledTransactions");
//
//        try {
//            String sql = String.format("SELECT * FROM transactions");
//            ResultSet query = openStatement.executeQuery(sql);
//            table.add(new ArrayList<>());
//            // System.out.println("Hello queryCancelledTransactions2");
//
//            while (query.next()) {
//                // System.out.println("Hello queryCancelledTransactions3");
//                // System.out.println(query.getString("user"));
//                table.get(0).add(query.getString("users"));
//            }
//        } catch(SQLException e) {
//            // if the error message is "out of memory",
//            // it probably means no database file is found
//            System.err.println(e.getMessage());
//        }
//
//        return table;
//    }


    /**
     * Function that allows for the username to be queried.
     * 
     * @return list
     */
    public ArrayList<String> queryUsername() {

        ArrayList<String> list = new ArrayList<>();

        try {
            String sql = String.format("SELECT username FROM users");
            ResultSet query = openStatement.executeQuery(sql);
            while (query.next()) {

                String uname = query.getString("username");
                list.add(uname);
            }
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return list;
    }

    public ArrayList<String> queryRoles() {

        ArrayList<String> list = new ArrayList<>();

        try {
            String sql = String.format("SELECT DISTINCT role FROM users");
            ResultSet query = openStatement.executeQuery(sql);
            while (query.next()) {

                String uname = query.getString("role");
                list.add(uname);
            }
        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return list;
    }


    /**
     * Method to remove a user
     * @param username
     * @return
     */
    public int removeUser(String username) {
        try {
            String sql = String.format("DELETE from users WHERE username = '%s';", username);
            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(sql);
            return 0;

        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return 1;

        }
    }


    /**
     * Function thay allows for a user's role to be changed.
     * 
     * @param username
     * @param role
     * 
     * @return
     */
    public boolean changeRole(String username, String role) {

        try {
            String sql = String.format("UPDATE users SET role = '%s' WHERE username = '%s';", role, username);
            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(sql);
            System.out.println("Changed " + username + " to " + role);
            return true;

        } catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }

    }


    /**
     * Function to add a new User into the database.
     * 
     * @param username  username of the new user
     * @param password  password of the new user
     * @param role      role of the new user
     * @return
     */
    public int insertNewUser(String username, String password, String role) {
        try {
            // SQLite does not strictly enforce the varchar limits, so we have to test for ourselves.
            if (username.length() > 15 || password.length() > 20 || password.length() < 8) {
                throw new SQLException("userName or password too short/long");
            }

            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            Password pw = PasswordHash.createHashedPassword(password);
            statement.executeUpdate(String.format("insert into users values('%s', '%s', '%s', '%s')", username, pw.hashPassword(), pw.strSalt(), role.toUpperCase()));
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;

        }

        return 0;
    }


    /**
     * Function to check if a certain user has the inputted role or not.
     * 
     * OWNER - O
     * CASHIER - C
     * GUEST - G
     * REGISTERED CUSTOMER - R
     * 
     * @param username  username one wants to check the role of
     * @param role      the role you want to check the username has
     * @return          true if user has the role, false if not 
     */
    public boolean checkRole(String username, String role) {

        try {
            String sql = String.format("select role from users where username  = '%s'", username);
            ResultSet query = openStatement.executeQuery(sql);
            if (query.getString("role").equalsIgnoreCase(role)) {
                return true;
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return false;
        }
        return false;
    }


    /**
     * Function to return the role of a specified user
     * 
     * @param username
     * @return
     */
    public String getRole(String username) {

        String sql = """
                SELECT role
                FROM Users
                WHERE username = '%s';
                """;
        try {
            ResultSet query = openStatement.executeQuery(String.format(sql, username));
            if (query.next()) {
                return query.getString("role");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while querying for the role :(");
            return "Error";
        }
    }


    /**
     * Function to validate a username
     * 
     * @param username username of the user
     * @return 0 if successful, -1 if unsuccessful
     */
    public int validateUsername(String username) {
        if (username.length() > 15) {
            return -1;
        }

        String sql = """
                SELECT *
                FROM Users
                WHERE username = '%s';
                """;

        try {
            ResultSet query = openStatement.executeQuery(String.format(sql, username));

            if (query.next()) {
                return 0;
            } else {
                return -1;
            }

        } catch (SQLException e) {
            System.out.println("Error while querying username :(");
            return -1;
        }
    }


    /**
     * Login function
     * 
     * @param username username of the user
     * @param password password of the user
     * @return 0 if successful, -1 if unsuccessful
     */
    public int login(String username, String password) {
        String sql = """
                SELECT salt, password
                FROM Users
                WHERE username = '%s';
                """;
        try {
            ResultSet query = openStatement.executeQuery(String.format(sql, username));
            
            if (query.next()) {
                if(PasswordHash.isValidUser(query.getString("salt"), query.getString("password"), password)) {
                    return 0;
                }
            }
            return -1;

        } catch (SQLException e) {
            System.out.println("Error while querying for user :(");
            return -1;

        }
    }


    /**
     * Function to get card number and cvv
     * 
     * @param username
     * @return card number and cvv in string array format (String[])
     */
    public String[] getCard(String username) {

        String sql = """
                SELECT card, cvv
                FROM cards
                WHERE username = '%s';
                """;
        try {
            ResultSet query = openStatement.executeQuery(String.format(sql, username));
            if (query.next()) {
                String[] details = {query.getString("card"), query.getString("cvv")};
                return details;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while querying for the card details :(");
            String[] errorMessage = {"Error", ""};
            return errorMessage;
        }
    }

    
    /**
     * Function to store user's card details in the database.
     * 
     * @param username  username of the user
     * @param card      card number of the user
     * @param cvv       cvv of the user
     * @return
     */
    public int insertNewCard(String username, String card, String cvv) {
        try {

            Statement statement = dbConn.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.

            statement.executeUpdate(String.format(
                "insert into cards values('%s', '%s', '%s')", 
                username, card, cvv));

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            return -1;
        }
        return 0;
    }


    /**
     * Function which returns all items currently in the database
     * @return itemList
     */
    public ArrayList<String[]> getAllItems() {

        ArrayList<String[]> itemList = new ArrayList<String[]>();

        String sql = """
                SELECT item_name, category_name
                FROM items
                """;
        try {
            ResultSet query = openStatement.executeQuery(sql);
            while (query.next()) {
                String[] item = new String[] {
                    query.getString("item_name"), 
                    query.getString("category_name")
                };
                itemList.add(item);
            }
        } catch (SQLException e) {
            return null;
        }

        return itemList;
    }

    /**
     * Function which returns all items currently in the database
     * @return itemList
     */
    public ArrayList<String[]> getItemSoldHistory() {

        ArrayList<String[]> itemList = new ArrayList<String[]>();

        String sql = """
                SELECT item_code, item_name, category_name, quantity, price, quantity_sold
                FROM items
                """;
        try {
            ResultSet query = openStatement.executeQuery(sql);
            while (query.next()) {
                String[] item = new String[] {
                    query.getString("item_code"), 
                    query.getString("item_name"), 
                    query.getString("category_name"),
                    query.getString("quantity"),
                    query.getString("price"),
                    query.getString("quantity_sold"),
                };
                itemList.add(item);
            }
        } catch (SQLException e) {
            return null;
        }

        return itemList;
    }

}

