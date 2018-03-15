package stockExchangeApp.controllers.forms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.market.StockExchange;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StockExchangeForm extends Pane implements Initializable{


    /**
     * Inicjalizuje liste krajow do comboboxa
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       countriesComboBox.getItems().clear();
       countriesComboBox.getItems().addAll(Economy.DB.getCountries());

    }


    @FXML
    private TextField name;

    @FXML
    private ComboBox countriesComboBox;

    @FXML
    private TextField city;

    @FXML
    private TextField address;

    @FXML
    private TextField margin;


    public StockExchangeForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/StockExchangeForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Dodaje gielde do "bazy danych"
     */
    public void onAdd(){
        Economy.DB.addStockExchange(new StockExchange(
                Economy.DB.getId(), Float.parseFloat(margin.getText()) , name.getText(),
                countriesComboBox.getSelectionModel().getSelectedItem().toString(),
                city.getText(), address.getText()));

    }

    public void onReset(){
    }

}

