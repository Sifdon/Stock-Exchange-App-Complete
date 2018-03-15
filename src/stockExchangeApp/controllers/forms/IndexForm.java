package stockExchangeApp.controllers.forms;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.market.StockExchange;
import stockExchangeApp.market.stockExchange.Company;
import stockExchangeApp.market.stockExchange.Index;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class IndexForm extends Pane implements Initializable{

    private int stockExchangeIndex;
    private String choice;
    private int companyIndex;
    private String company;
    private ArrayList<Company> companies;

    /**
     * Inicjalizacja ComboBoxa gieldy, blokowanie wyswietlania dalszych operacji przed wyborem gieldy
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> stockExchanges = new ArrayList<>();
        companies = new ArrayList<>();

        shareValueLabel.visibleProperty().setValue(false);
        shareValueAbove.visibleProperty().setValue(false);
        companiesLabel.visibleProperty().setValue(false);
        addToIndexButton.visibleProperty().setValue(false);
        companiesComboBox.visibleProperty().setValue(false);
        membershipLabel.visibleProperty().setValue(false);
        conditionComboBox.visibleProperty().setValue(false);

        for(StockExchange stockExchange : Economy.DB.getStockExchanges()){
            stockExchanges.add(stockExchange.getName());
        }

        stockExchangeComboBox.getItems().clear();
        stockExchangeComboBox.getItems().addAll(stockExchanges);
        listenToStockExchanges();


        conditionComboBox.getItems().clear();
        conditionComboBox.getItems().addAll("User's choice","Share value above");
        listenToCondition();
    }

    /**
     * Mointoruje zmiany w wyborze gieldy
     */
    private void listenToStockExchanges(){
        stockExchangeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stockExchangeIndex = stockExchangeComboBox.getSelectionModel().getSelectedIndex();
                membershipLabel.visibleProperty().setValue(true);
                conditionComboBox.visibleProperty().setValue(true);
            }
        });
    }

    /**
     * Monitoruje wybor zasady dziala indeksu
     */
    private void listenToCondition(){
        conditionComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                choice = newValue;
                displayCondition(choice);
            }
        });
    }

    /**
     * Monitoruje wybor spolki
     */
    private void listenToCompanies(){
        companiesComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                company = newValue;
                companyIndex = companiesComboBox.getSelectionModel().getSelectedIndex();
            }
        });
    }

    /**
     * Inicjalizuje spolki
     */
    private void initCompaniesComboBox(){
        companiesComboBox.getItems().clear();
        ArrayList<String> companiesNames = new ArrayList<>();
        for(Company company : Economy.DB.getStockExchanges().get(stockExchangeIndex).getCompanies()){
            companiesNames.add(company.getName());
        }

        companiesComboBox.getItems().addAll(companiesNames);
        listenToCompanies();
    }

    /**
     * Odpowiada za pokazywanie wlasciwego formularza
     * @param choice wybrany warunku
     */
    private void displayCondition(String choice){
        if(choice.equals("Share value above")){
            companies.clear();
            shareValueLabel.visibleProperty().setValue(true);
            shareValueAbove.visibleProperty().setValue(true);
            companiesComboBox.visibleProperty().setValue(false);
            companiesLabel.visibleProperty().setValue(false);
            addToIndexButton.visibleProperty().setValue(false);
        }
        else{
            companies.clear();
            shareValueLabel.visibleProperty().setValue(false);
            companiesComboBox.visibleProperty().setValue(true);
            companiesLabel.visibleProperty().setValue(true);
            addToIndexButton.visibleProperty().setValue(true);
            shareValueAbove.visibleProperty().setValue(false);
            initCompaniesComboBox();
        }
    }

    /**
     * Przypisuje do ineksu spolki o odpowiednio drogich akcjach
     */
    private void biggestCompanies(){
            companies.clear();
            for (Company company : Economy.DB.getStockExchanges().get(stockExchangeIndex).getCompanies()) {
                if (company.getCurrentExchangeRate() > Float.parseFloat(shareValueAbove.getText())) {
                    companies.add(company);
                }
            }
    }

    /**
     * Dodaje wybrana spolke do indexu
     */
    public void onAddToIndex(){
        if(company != null){
            companies.add(Economy.DB.getStockExchanges().get(stockExchangeIndex).getCompanies().get(companyIndex));
        }
    }


    @FXML
    private TextField shareValueAbove;

    @FXML
    private ComboBox stockExchangeComboBox;

    @FXML
    private ComboBox conditionComboBox;

    @FXML
    private Pane conditionPane;

    @FXML
    private Label companiesLabel;

    @FXML
    private ComboBox companiesComboBox;

    @FXML
    private Button addToIndexButton;

    @FXML
    private Label membershipLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label shareValueLabel;

    public IndexForm() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/forms/IndexForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Dodaje indeks do wybranej gieldy
     */
    public void onAdd(){
        if(choice.equals("Share value above")){
            biggestCompanies();
        }
        Economy.DB.getStockExchanges().get(stockExchangeIndex).getIndexes().add(new Index(Economy.DB.getId(),
                nameTextField.getText(), companies, Economy.DB.getStockExchanges().get(stockExchangeIndex)));
    }

    public void onReset(){

    }

}

