package VendingMachine.pages;

import VendingMachine.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.apache.commons.logging.Log;


public class SuccessfulPage extends  Page{

    private SceneManager sm;
    private StackPane pane;

    private Text change;
    private Button continue_Shopping;
    private Button logOut;

    private StringProperty textBind = new SimpleStringProperty();


    /**
     * Constructor for SuccessfulPage screen.
     * @param sm
     */
    public SuccessfulPage(SceneManager sm) {
        this.sm = sm;

        pane = new StackPane();
        scene = new Scene(pane, WIDTH, HEIGHT);

        VBox box = new VBox();
        box.setSpacing(5);
        box.setPrefWidth(150);
        box.setAlignment(Pos.CENTER);

        Text title = new Text();
        title.setText("Transaction Successful!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 35));

        Text message = new Text();
        message.textProperty().bind(textBind);

        continue_Shopping = new Button("Continue Shopping");
        logOut = new Button("Log Out");

        box.getChildren().addAll(title, message, continue_Shopping, logOut);
        pane.getChildren().add(box);

        continue_Shopping.setOnAction(event -> {
            sm.getDefaultPageController().clearTransaction();
            sm.switchScenes(sm.getDefaultPageScene());
        });

        // Logout on cancelled transaction
        logOut.setOnAction(event -> {
            sm.switchScenes(sm.getDefaultPageScene());
            sm.getDefaultPageController().logout();
        });

    }


    /**
     * Setting up the so that you can change as required the text page.
     * @param text
     */
    public void setMessage(String text){
        textBind.set(text);
    }

}
