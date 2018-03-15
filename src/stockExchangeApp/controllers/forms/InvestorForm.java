package stockExchangeApp.controllers.forms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.trader.Investor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InvestorForm extends Pane implements Initializable{

    /**
     * Losuje wartosci pol
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        randomizeValues();
    }

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField pesel;

    @FXML
    private TextField budget;

    public InvestorForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/InvestorForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Losuje wartosci pol inwerstora
     */
    private void randomizeValues(){
        firstName.setText(Economy.Rndm.randomizeName("firstName"));
        lastName.setText(Economy.Rndm.randomizeName("lastName"));
        pesel.setText(Economy.Rndm.randomizePesel());
        budget.setText(Float.toString(Economy.Rndm.randomizeNumber(100000)));
    }

    /**
     * Dodaje inwestora do bazy danych
     */
    public void onAdd(){
        Economy.DB.addInvestor(new Investor(Economy.DB.getId(), firstName.getText(), lastName.getText(),
                pesel.getText(), Float.parseFloat(budget.getText())));
    }

    public void onReset(){
        randomizeValues();
    }



}

