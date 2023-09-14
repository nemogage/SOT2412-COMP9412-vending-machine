package VendingMachine;

public class Session {

    private String userName = "Guest";
    private String role = "Guest";
    private boolean startedTransaction;
    private final int TICK_LIMIT = 120;
    private int tick;
    private boolean loggedIn;
    private Transaction transaction = new Transaction();

    // Values
//    private double totalPrice;
//
//    private double changeAmount;
//
//    private double dueAmount;


    public Session(){
    }

    /**
     * Returns the username of the session
     * @return user Name ( String )
     */
    public String getUserName() {
        return this.userName;
    }


    /**
     * Function to return the role of current session user
     * @return role ( String )
     */
    public String getRole() {
        return this.role;
    }


    /**
     * Function to return the state of the transaction, has it been started or not.
     * @return whether the transaction has been started or not ( boolean )
     */
    public boolean getTransactionState() {
        return this.startedTransaction;
    }


    /**
     * Function to set the state of the transaction.
     * @param startedTransaction ( Boolean )
     */
    public void setStartedTransaction(boolean startedTransaction) {
        this.startedTransaction = startedTransaction;
    }

//
//    /**
//     * Function that returns the total price
//     * @return double
//     */
//    public double getTotalPrice() {
//        return totalPrice;
//    }
//
//
//    /**
//     * Function that sets the totals price.
//     * @param totalPrice
//     */
//
//    public void setTotalPrice(double totalPrice) {
//        this.totalPrice = totalPrice;
//    }
//
//
//    /**
//     * Function that return the due amount
//     * @return dueAmount (double)
//     */
//    public double getDueAmount() {
//        return dueAmount;
//    }
//
//
//    /**
//     * Function that return the change amount
//     * @return changeAmount (double)
//     */
//    public double getChangeAmount() {
//        return changeAmount;
//    }
//
//
//    /**
//     *  Function that set the due amount
//     * @param dueAmount
//     */
//    public void setDueAmount(double dueAmount) {
//        if( dueAmount < 0 ) {
//            this.dueAmount = 0;
//            return;
//        }
//        this.dueAmount = dueAmount;
//    }
//
//
//    /**
//     * Function that sets the change amount
//     * @param changeAmount
//     */
//    public void setChangeAmount(double changeAmount) {
//        if( changeAmount < 0 ){
//            this.changeAmount = 0;
//            return;
//        }
//        this.changeAmount = changeAmount;
//    }
//
//
//    /**
//     * Function that resets all the amounts to 0
//     */
//    public void resetAmount(){
//        totalPrice = 0;
//        dueAmount = 0;
//        changeAmount = 0;
//    }

    
    /**
     * Function to set the role for a session.
     * @param role (String)
     */
    public void setRole(String role) {
        this.role = role;
        if (role.equalsIgnoreCase("guest")) {
            this.setLoggedIn( false);
        }
        else this.setLoggedIn(true);
    }


    /**
     * Function to set the userName for the session.
     * @param userName ( String )
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    // todo, it is hardcoded atm.

    /**
     * Function to reset the role and user back to guest.
     */
    public void resetSession() {
        loggedIn = false;
        userName = "Guest";
        role = "Guest";
    }


    public void setLoggedIn(boolean bool) {
        this.loggedIn = bool;
    }


    /**
     * Returns the logged in value
     * @return
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }


    /**
     * Returns the transaction
     * @return
     */
    public Transaction getTransaction() {
        return transaction;
    }
}
