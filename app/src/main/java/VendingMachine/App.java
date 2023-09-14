package VendingMachine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class App extends Application {

    private Stage primaryStage;

    /**
     * Start method for the Vending Machine application.
     */
    @Override
    public void start(Stage primaryStage0) throws Exception {

        primaryStage = primaryStage0;
        // Stage is basically the window, and you are given the name of the window.
        primaryStage.setTitle("Vending Machine");

        // we do not want the window to be resizable/
        primaryStage.setResizable(false);


        // If you want to add an icon for the program:
        primaryStage.getIcons().add(new Image("images/logo.jpg"));

        // with javafx, by default with window will appear in the middle, unlike Swing, so no changes are needed.
        // String sceneFile = "/fxml/Main.fxml";
        // Parent root2= null;
        // URL    url2 = null;
        // try
        // {
        //     url2 = getClass().getResource( sceneFile );
        //     root2= FXMLLoader.load( url2 );
        //     System.out.println( "  fxmlResource = " + sceneFile );
        // }
        // catch ( Exception ex )
        // {
        //     System.out.println( "Exception on FXMLLoader.load()" );
        //     System.out.println( "  * url: " + url2 );
        //     System.out.println( "  * " + ex );
        //     System.out.println( "    ----------------------------------------\n" );
        // }


        URL url = getClass().getResource("/fxml/Main.fxml");
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(url);

        // DefaultPageController defaultPageController = loader.getController();
        // defaultPageController.setSceneManager(sceneManager);
        // primaryStage.setScene(sceneManager.getDefaultPageScene());

        Scene scene = new Scene(root);
        String css = getClass().getResource("/css/style.css").toExternalForm();

        scene.getStylesheets().add(css);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main function to launch the Vending Machine app.
     */
    public static void main(String[] args) {
        launch(args);
    }

}