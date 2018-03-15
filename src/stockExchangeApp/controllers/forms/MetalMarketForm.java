package stockExchangeApp.controllers.forms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.market.MetalExchange;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MetalMarketForm extends Pane {

    @FXML
    private TextField name;

    @FXML
    private TextField margin;

    public MetalMarketForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/MetalMarketForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Dodaje rynek surowcow do "bazy danych"
     */
    public void onAdd(){
        Economy.DB.addMetalExchange(new MetalExchange(Economy.DB.getId(), name.getText(), Float.parseFloat(margin.getText())));
    }

    public void onReset(){
    }


}

