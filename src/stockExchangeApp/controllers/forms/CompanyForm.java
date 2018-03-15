package stockExchangeApp.controllers.forms;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.market.stockExchange.Company;
import stockExchangeApp.market.StockExchange;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CompanyForm extends Pane implements Initializable{

    private int stockExchangeIndex;

    /**
     * Dodaje istniejace gieldy do comboboxa
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> stockExchanges = new ArrayList<>();
        for(StockExchange stockExchange : Economy.DB.getStockExchanges()){
            stockExchanges.add(stockExchange.getName());
        }

        stockExchangeComboBox.getItems().clear();
        stockExchangeComboBox.getItems().addAll(stockExchanges);
        listenToStockExchanges();
        randomizeValues();
    }

    /**
     * Sprawdza ktora gielda zostala wybrana
     */
    private void listenToStockExchanges(){
        stockExchangeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stockExchangeIndex = stockExchangeComboBox.getSelectionModel().getSelectedIndex();
            }
        });
    }

    @FXML
    private TextField name;

    @FXML
    private TextField openingExchangeRate;

    @FXML
    private TextField currentExchangeRate;

    @FXML
    private TextField minExchangeRate;

    @FXML
    private TextField maxExchangeRate;

    @FXML
    private TextField numberOfShares;

    @FXML
    private TextField income;

    @FXML
    private TextField profit;

    @FXML
    private TextField equityCapital;

    @FXML
    private TextField shareCapital;

    @FXML
    private TextField volume;

    @FXML
    private TextField sales;

    @FXML
    private ComboBox stockExchangeComboBox;



    public CompanyForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/CompanyForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Losuje wartosci pol spolki
     */
    private void randomizeValues(){
        Random generator = new Random();
        float[] prices = Economy.Rndm.randomizePrices(1000);
        name.setText(Economy.Rndm.randomizeName("companyName"));
        openingExchangeRate.setText(Float.toString(prices[1]));
        currentExchangeRate.setText(Float.toString(prices[1]));
        minExchangeRate.setText(Float.toString(prices[0]));
        maxExchangeRate.setText(Float.toString(prices[2]));
        numberOfShares.setText(Integer.toString(generator.nextInt(100000)));
        income.setText(Float.toString(Economy.Rndm.randomizeNumber(10000)));
        profit.setText(Float.toString(Economy.Rndm.randomizeNumber(10000)));
        equityCapital.setText(Float.toString(Economy.Rndm.randomizeNumber(10000)));
        shareCapital.setText(Float.toString(Economy.Rndm.randomizeNumber(10000)));
        volume.setText(Integer.toString(generator.nextInt(10000)));
        sales.setText(Float.toString(Economy.Rndm.randomizeNumber(10000)));

    }

    /**
     * Dodaje wprowadzana spolke do "bazy danych"
     */
    public void onAdd(){
        int companyId = Economy.DB.getId();
        Company company = new Company(
                companyId, name.getText(),  Float.parseFloat(openingExchangeRate.getText()),
                Float.parseFloat(currentExchangeRate.getText()), Float.parseFloat(minExchangeRate.getText()),
                Float.parseFloat(maxExchangeRate.getText()), Integer.parseInt(numberOfShares.getText()),
                Float.parseFloat(income.getText()), Float.parseFloat(profit.getText()),
                Float.parseFloat(equityCapital.getText()), Float.parseFloat(shareCapital.getText()),
                Integer.parseInt(volume.getText()), Float.parseFloat(sales.getText()), Economy.DB.getStockExchanges().get(stockExchangeIndex)
        );
        Economy.DB.getStockExchanges().get(stockExchangeIndex).getCompanies().add(company);
        Economy.DB.getStockExchanges().get(stockExchangeIndex).setEmptyMarket(false);
        Economy.DB.getCompaniesObservableList().add(company);
    }

    public void onReset(){
        randomizeValues();
    }

}

