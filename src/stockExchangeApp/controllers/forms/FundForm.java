package stockExchangeApp.controllers.forms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.trader.Fund;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FundForm extends Pane implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        randomizeValues();
    }

    @FXML
    private TextField name;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField budget;

    public FundForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/FundForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Losuje wartosci pol funduszu
     */
    private void randomizeValues(){
        firstName.setText(Economy.Rndm.randomizeName("firstName"));
        lastName.setText(Economy.Rndm.randomizeName("lastName"));
        name.setText(Economy.Rndm.randomizeName("fundName"));
        budget.setText(Float.toString(Economy.Rndm.randomizeNumber(100000)));
    }

    /**
     * Dodaje fundusz do "bazy danych"
     */
    public void onAdd(){
        Economy.DB.addFund(new Fund(Economy.DB.getId(), Float.parseFloat(budget.getText()),
                name.getText(), firstName.getText(), lastName.getText()));
    }

    public void onReset(){
        randomizeValues();
    }
}

