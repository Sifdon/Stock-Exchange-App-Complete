package stockExchangeApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Serializable;

public class Economy extends Application implements Serializable{

    public static DataStorage DB;

    public static Randomizer Rndm;

    public static ThreadsManager ThreadsMngr;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DataStorage dataStorage = new DataStorage();
        DB = dataStorage;
        Randomizer randomizer = new Randomizer();
        Rndm = randomizer;
        ThreadsManager threadsManager = new ThreadsManager();
        ThreadsMngr = threadsManager;

        Parent root = FXMLLoader.load(getClass().getResource("views/starterWindow.fxml"));
        primaryStage.setTitle("StockExchange App");
        Scene scene = new Scene(root, 975,645);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
