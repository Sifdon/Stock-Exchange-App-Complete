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
import stockExchangeApp.DataStorage;
import stockExchangeApp.Economy;
import stockExchangeApp.ThreadsManager;
import stockExchangeApp.trader.Fund;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maciej on 06.01.2018.
 */
public class FundInfoController implements Initializable{
    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label budgetLabel;

    @FXML
    private Label walletSizeLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Button deleteButton;

    private Fund fund;

    private Stage window;

    /**
     * Wyswietla inforamcje o funduszu
     */
    public void display(){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/stockExchangeApp/views/Objects/FundInfo.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 600, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Pobiera informacje o funduszu
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Fund fund : Economy.DB.getFundsObservableList()) {
            if (fund.getId() == Economy.DB.getIdOfFundToDisplay()) {
                this.fund = fund;
            }
        }
            firstNameLabel.setText(fund.getManagersFirstName());
            lastNameLabel.setText(fund.getManagersLastName());
            budgetLabel.setText(Float.toString(fund.getBudget()));
            walletSizeLabel.setText(Integer.toString(fund.getWallet().getAssets().size()));
            nameLabel.setText(fund.getName());
    }

    public void onDelete(){
        Thread threadToDelete = null;
        for(Thread thread : Economy.DB.getThreads()){
            if(fund.getId() == thread.getId()){
                threadToDelete = thread;
            }
        }
        if(threadToDelete != null) {
            Economy.DB.getThreads().remove(threadToDelete);
        }
        Economy.DB.getFunds().remove(fund);
        Economy.DB.getFundsObservableList().remove(fund);
    }
}
