package stockExchangeApp.controllers.objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stockExchangeApp.Economy;
import stockExchangeApp.trader.Investor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maciej on 06.01.2018.
 */
public class InvestorInfoController implements Initializable{
    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label budgetLabel;

    @FXML
    private Label walletSizeLabel;

    @FXML
    private Button deleteButton;


    private Investor investor;

    private Stage window;

    /**
     * Wyswietla informacje o inwestorze w nowym oknie
     */
    public void display(){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/stockExchangeApp/views/Objects/InvestorInfo.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 600, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Pobiera dane o inwestorze
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Investor investor : Economy.DB.getInvestorObservableList()) {
            if (investor.getId() == Economy.DB.getIdOfInvestorToDisplay()) {
                this.investor = investor;
            }
        }
            firstNameLabel.setText(investor.getFirstName());
            lastNameLabel.setText(investor.getLastName());
            budgetLabel.setText(Float.toString(investor.getBudget()));
            walletSizeLabel.setText(Integer.toString(investor.getWallet().getAssets().size()));
    }

    public void onDelete(){
        Thread threadToDelete = null;
        for(Thread thread : Economy.DB.getThreads()){
            if(investor.getId() == thread.getId()){
                threadToDelete = thread;
            }
        }
        if(threadToDelete != null){
            Economy.DB.getThreads().remove(threadToDelete);
        }
        Economy.DB.getInvestors().remove(investor);
        Economy.DB.getInvestorObservableList().remove(investor);
    }
}
