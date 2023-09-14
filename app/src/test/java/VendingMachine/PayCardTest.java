package VendingMachine;

import VendingMachine.pages.PayCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import VendingMachine.*;

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

// JSON Reader
import org.json.simple.*;
import org.json.simple.parser.*;

// OpenCSV import
import com.opencsv.*;

public class PayCardTest {

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public long getRandomLong() {
        long leftLimit = 1000000000000000L;
        long rightLimit = 9999999999999999L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return generatedLong;
    }

    // Test for valid card number
    @Test
    void testCardNumber() {
        for (int i = 0; i <= 100000; i++) {
            long cardNumber = getRandomLong();
            assertTrue(PayCard.checkCardNumber(Long.toString(cardNumber)));
        }
    }

    // Test for valid CVV
    @Test
    void testCVV() {
        for (int i = 0; i <= 100; i++) {
            int CVV = getRandomNumber(100, 999);
            assertTrue(PayCard.checkCVV(Integer.toString(CVV)));
        }
    }

}
