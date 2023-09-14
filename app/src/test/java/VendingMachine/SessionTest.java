package VendingMachine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionTest {

    Session sess;

    // setting up before each test
    @BeforeEach
    void setUp(){
        sess = new Session();
    }

    @AfterEach
    void tearDown(){
        sess = null;
    }

    // Setting set role.
    // Test through in the next sprint.
    @Test
    void getSetRoleTest(){
        sess.setRole("Owner");
        assertEquals("Owner", sess.getRole());
    }

    // Checking toggle on logged in
    @Test
    void sessionRoleNonGuestTest(){
         sess.setRole("Owner");

         assertTrue(sess.isLoggedIn());
    }

    @Test
    void sessionRoleGuestTest(){
        sess.setRole("guest");

        assertFalse(sess.isLoggedIn());
    }

    // userName Test
    @Test
    void userNameTest(){
        sess.setUserName("user1");

        assertEquals("user1",sess.getUserName());

    }
}
