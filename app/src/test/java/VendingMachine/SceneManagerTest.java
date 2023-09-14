package VendingMachine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SceneManagerTest {

    private SceneManager sceneManager;

    // You want to open a new API to the database
    // and drop all tables so you are working with a empty relations for each test.
    @BeforeEach
    void setUp(){
        //sceneManager = new SceneManager();
    }


    // Get rid of the database
    @AfterEach
    void tearDown(){
        sceneManager = null;
    }

    @Test
    void testSceneManagerExists(){
       // assertNotNull(sceneManager);
    }

}
