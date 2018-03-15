package stockExchangeApp.controllers.forms;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.asset.Metal;
import stockExchangeApp.market.MetalExchange;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;



public class MetalForm extends Pane implements Initializable {

    private int currencyId;
    private int metalMarketIndex;

    /**
     * Iniclaizuje wartosci comboBoxa rynkow surowcow
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> metalMarkets = new ArrayList<>();
        for(MetalExchange metalExchange : Economy.DB.getMetalExchanges()){
            metalMarkets.add(metalExchange.getName());
        }

        metalMarketComboBox.getItems().clear();
        metalMarketComboBox.getItems().addAll(metalMarkets);
        listenToMetalMarkets(metalMarkets);
        randomizeValues();
    }

    /**
     * Monitoruje zmiany w wyborze rynku surowcow
     * @param metalMarkets wybrany rynek surowcow
     */
    private void listenToMetalMarkets(ArrayList<String> metalMarkets){
        metalMarketComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                metalMarketIndex = metalMarketComboBox.getSelectionModel().getSelectedIndex();
            }
        });

    }

    @FXML
    private TextField name;

    @FXML
    private TextField tradeUnit;

    @FXML
    private TextField currentPrice;

    @FXML
    private TextField minPrice;

    @FXML
    private TextField maxPrice;

    @FXML
    private ComboBox metalMarketComboBox;



    public MetalForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/MetalForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Losuje wartosci pol metalu
     */
    private void randomizeValues() {
        float[] prices = Economy.Rndm.randomizePrices(1000);
        Random generator = new Random();
        name.setText(Economy.Rndm.randomizeName("metalName"));
        tradeUnit.setText(Economy.Rndm.randomizeName("unitName"));
        currentPrice.setText(Float.toString(prices[1]));
        minPrice.setText(Float.toString(prices[0]));
        maxPrice.setText(Float.toString(prices[2]));
    }

    /**
     * Dodaje metal do "bazy danych"
     */
    public void onAdd(){
        int metalId = Economy.DB.getId();
        Metal metal = new Metal(metalId, name.getText(), tradeUnit.getText(), Float.parseFloat(currentPrice.getText()),
                Float.parseFloat(minPrice.getText()), Float.parseFloat(maxPrice.getText()),
                Economy.DB.getMetalExchanges().get(metalMarketIndex));

        Economy.DB.getMetalExchanges().get(metalMarketIndex).getMetalsList().add(metal);
        Economy.DB.getMetals().add(metal);
        Economy.DB.getMetalExchanges().get(metalMarketIndex).setEmptyMarket(false);
    }

    public void onReset(){
        randomizeValues();
    }


}


