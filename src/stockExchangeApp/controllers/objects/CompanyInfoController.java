package stockExchangeApp.controllers.objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stockExchangeApp.Economy;
import stockExchangeApp.market.stockExchange.Company;
import stockExchangeApp.trader.Trader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maciej on 30.12.2017.
 */
public class CompanyInfoController implements Initializable{

    @FXML
    private Label nameLabel;

    @FXML
    private Label curPriceLabel;

    @FXML
    private Label minPriceLabel;

    @FXML
    private Label maxPriceLabel;

    @FXML
    private Label incomeLabel;

    @FXML
    private javafx.scene.control.TextField repurchasePrice;

    Company company;

    Stage window;

    /**
     * Wyswietla dane o spolce
     */
    public void display(){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/stockExchangeApp/views/Objects/CompanyInfo.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 600, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Odpala operacje wykupywania akcji spolki
     */
    public void onRepurchase(){
        company.repurchaseShares(Float.parseFloat(repurchasePrice.getText()));
    }

    /**
     * Usuwa spolke z rynku
     */
    public void onDelete(){
        company.getStockExchange().getCompanies().remove(company);
        Economy.DB.getCompaniesObservableList().remove(company);
        if(company.getStockExchange().getCompanies().size() == 0){
            company.getStockExchange().setEmptyMarket(true);
        }

        if(company.getTradersWithShares().size() > 0) {

            Trader traders[] = new Trader[company.getTradersWithShares().size()];
            company.getTradersWithShares().toArray(traders);


            for (Trader trader : traders) {
                int indexOfShareBeingRemoved = trader.getWallet().indexOfAsset(company.getShare());
                trader.getWallet().getAssets().remove(indexOfShareBeingRemoved);
            }

        }

        Economy.DB.getShares().remove(company.getShare());
        company.setShare(null);

        Thread threadToDelete = null;
        for(Thread thread : Economy.DB.getThreads()){
            if(thread.getId() == company.getId()){
                threadToDelete = thread;
                thread.interrupt();
            }
        }
        if(threadToDelete != null){
            Economy.DB.getThreads().remove(threadToDelete);
        }
        company = null;
    }

    /**
     * Pobiera inforamcje o spolce
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Company company : Economy.DB.getCompaniesObservableList()) {
            if (company.getId() == Economy.DB.getIdOfCompanyToDisplay()) {
                this.company = company;
            }
        }
                nameLabel.setText(company.getName());
                curPriceLabel.setText(Float.toString(company.getCurrentExchangeRate()));
                minPriceLabel.setText(Float.toString(company.getMinExchangeRate()));
                maxPriceLabel.setText(Float.toString(company.getMaxExchangeRate()));
                incomeLabel.setText(Float.toString(company.getIncome()));
    }
}
