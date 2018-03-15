package stockExchangeApp.controllers.forms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.market.ForeignExchange;

import java.io.IOException;

public class ForexForm extends Pane {


    @FXML
    private TextField name;

    @FXML
    private TextField margin;



    public ForexForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/ForexForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Dodaje rynek walut do bazy danych
     */
    public void onAdd(){
        Economy.DB.addForeignExchange(new ForeignExchange(Economy.DB.getId(), name.getText(),
                Float.parseFloat(margin.getText())));
        if(Economy.DB.getCurrencies().size() > 0){
            Economy.DB.getForeignExchange().setEmptyMarket(false);
        }
    }

    public void onReset(){
    }


}

