package stockExchangeApp.controllers.forms;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.asset.Currency;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class CurrencyForm extends Pane implements Initializable {

    private String country;
    private ArrayList<String> countries;

    /**
     * Inicjalizuje liste krajow do comboboxa
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        countries = new ArrayList<>();
        countriesComboBox.getItems().clear();
        countriesComboBox.getItems().addAll(Economy.DB.getCountries());
        listenToAddNewCountry();

        randomizeValues();
    }

    /**
     * Sprawdza ktory kraj zostal wybrany
     */
    private void listenToAddNewCountry(){
        countriesComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                country = newValue;
            }
        });
    }

    @FXML
    private TextField name;

    @FXML
    private ComboBox countriesComboBox;

    /**
     * Losuje wartosci pol waluty
     */
    private void randomizeValues(){
        name.setText(Economy.Rndm.randomizeName("currencyName"));
    }

    public CurrencyForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/CurrencyForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * dodaje kraje do listy krajow
     */
    public void onAddCountry(){
        if(country != null){
            if(!countries.contains(country)){
                countries.add(country);
            }
        }
    }

    public void onReset(){
        randomizeValues();
    }

    /**
     * Dodaje walute do "bazy danych"
     */
    public void onAdd(){
        Currency currency = new Currency(Economy.DB.getId(), name.getText(), countries);
        Economy.DB.addCurrency(currency);
        if(Economy.DB.getForeignExchange() != null){
            Economy.DB.getForeignExchange().addNewCurrency(currency);
            Economy.DB.getForeignExchange().setEmptyMarket(false);
        }
    }


}


