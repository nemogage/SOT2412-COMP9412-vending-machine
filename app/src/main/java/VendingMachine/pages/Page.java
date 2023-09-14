package VendingMachine.pages;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public abstract class Page {

    // Set constants
    public final int WIDTH = 1280;
    public final int HEIGHT = 720;

    public final int BUTTONWIDTH = WIDTH / 8;
    public final int BUTTONHEIGHT = HEIGHT / 8;

    public final double PREFWIDTH = 190.00;
    public final int SPACING = 5;

    public Scene scene;

    /**
     * Return the page scene
     */
    public Scene getScene() {
        return scene;
    }

}
