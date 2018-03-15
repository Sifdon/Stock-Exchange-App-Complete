package stockExchangeApp.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import stockExchangeApp.Economy;
import stockExchangeApp.controllers.charts.MultiChartController;
import stockExchangeApp.controllers.forms.*;
import stockExchangeApp.controllers.mainPage.CurrencyTable;
import stockExchangeApp.controllers.mainPage.MetalTable;
import stockExchangeApp.controllers.mainPage.ShareTable;
import stockExchangeApp.controllers.objects.CompaniesTable;
import stockExchangeApp.controllers.objects.FundsTable;
import stockExchangeApp.controllers.objects.InvestorsTable;
import stockExchangeApp.serialization.Serialization;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private ComboBox<String> addNewComboBox;

    @FXML
    private Pane formPane;

    @FXML
    private Pane assetPane;

    @FXML
    private Button start;

    @FXML
    private Button stop;

    @FXML
    private Pane objectsPane;

    @FXML
    private CheckBox multiAsset;

    @FXML
    private Button showChart;

    @FXML
    private Button serializeButton;

    @FXML
    private TextField savingFileName;

    @FXML
    private TextField loadingFileName;

    @FXML
    private Button deserializeButton;


    /**
     * Dodaje kolejne tabele do widoku, dodaje nazwy obietkow do comboboxa sluzacego do tworzenia nowych obiektow
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addNewComboBox.getItems().clear();
        addNewComboBox.getItems().addAll("Stock Exchange", "Index", "Company", "Currency",
                "Metal", "Investor", "Fund", "Metal Market", "Forex");
        listenToAddNewComboBox();

        assetPane.getChildren().clear();
        objectsPane.getChildren().clear();

        InvestorsTable investorsTable =  new InvestorsTable();
        objectsPane.getChildren().add(investorsTable);
        investorsTable.setLayoutX(0);
        investorsTable.setLayoutY(0);

        FundsTable fundsTable =  new FundsTable();
        objectsPane.getChildren().add(fundsTable);
        fundsTable.setLayoutX(0);
        fundsTable.setLayoutY(180);

        CompaniesTable companiesTable = new CompaniesTable();
        objectsPane.getChildren().add(companiesTable);
        companiesTable.setLayoutX(0);
        companiesTable.setLayoutY(360);

        ShareTable shareTable = new ShareTable();
        assetPane.getChildren().add(shareTable);
        shareTable.setLayoutX(0);
        shareTable.setLayoutY(0);


        MetalTable metalTable = new MetalTable();
        assetPane.getChildren().add(metalTable);
        metalTable.setLayoutX(0);
        metalTable.setLayoutY(180);

        CurrencyTable currencyTable = new CurrencyTable();
        assetPane.getChildren().add(currencyTable);
        currencyTable.setLayoutX(0);
        currencyTable.setLayoutY(360);

        multiAsset.setLayoutX(560);
        multiAsset.setLayoutY(560);

        showChart.setLayoutX(780);
        showChart.setLayoutY(560);

        showChart.visibleProperty().setValue(false);


    }

    /**
     * Monitoruje jaki obiekt wybrano do stworzenia z comboboxa
     */
    private void listenToAddNewComboBox(){
        addNewComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                displayForm(newValue);
            }
        });
    }

    /**
     * Generuje odpowiedni formularz do tworzenia obiektu w zaleznosci jaki obiekt wybrano
     * @param formName nazwa formularza
     */
    private void displayForm(String formName){
        switch (formName){

            case "Stock Exchange":
                formPane.getChildren().clear();
                StockExchangeForm stockExchangeForm = new StockExchangeForm();
                formPane.getChildren().add(stockExchangeForm);
                break;

            case "Investor":
                formPane.getChildren().clear();
                InvestorForm investorForm = new InvestorForm();
                formPane.getChildren().add(investorForm);
                break;

            case "Fund":
                formPane.getChildren().clear();
                FundForm fundForm = new FundForm();
                formPane.getChildren().add(fundForm);
                break;

            case "Index":
                formPane.getChildren().clear();
                IndexForm indexForm = new IndexForm();
                formPane.getChildren().add(indexForm);
                break;

            case "Company":
                formPane.getChildren().clear();
                CompanyForm companyForm = new CompanyForm();
                formPane.getChildren().add(companyForm);
                break;

            case "Currency":
                formPane.getChildren().clear();
                CurrencyForm currencyForm = new CurrencyForm();
                formPane.getChildren().add(currencyForm);
                break;

            case "Metal":
                formPane.getChildren().clear();
                MetalForm metalForm = new MetalForm();
                formPane.getChildren().add(metalForm);
                break;

            case "Forex":
                formPane.getChildren().clear();
                ForexForm forexForm = new ForexForm();
                formPane.getChildren().add(forexForm);
                break;

            case "Metal Market":
                formPane.getChildren().clear();
                MetalMarketForm metalMarketForm = new MetalMarketForm();
                formPane.getChildren().add(metalMarketForm);
                break;

             default:
                 break;
        }
    }

    /**
     * Uruchamia watki
     */
    public void onStart(){
        Economy.ThreadsMngr.startThreads();
    }

    /**
     * Przelacza pomiedzy trybem multi a single asset do wyswietlania wykresow
     */
    public void onMultiAsset(){
        showChart.visibleProperty().setValue(!showChart.isVisible());
        Economy.DB.setMultiChartModeOn(!Economy.DB.isMultiChartModeOn());
        if(!Economy.DB.isMultiChartModeOn()){
            Economy.DB.getChartsXValues().clear();
            Economy.DB.getChartsYValues().clear();
            Economy.DB.getSeriesNames().clear();
        }
    }

    /**
     * Po klinkieciu, tworzy wykres dla wielu assetow
     */
    public void onShowChart(){
        MultiChartController test = new MultiChartController();
        test.display();
    }

    /**
     * Stopuje wątki
     */
    public void onStop(){
        Economy.ThreadsMngr.stopThreads();
    }

    /**
     * Uruchamia serializacje do podanego pliku
     */
    public void onSerialize(){
        Serialization serialization = new Serialization();
        serialization.serialize(savingFileName.getText());
    }

    /**
     * Uruchamia deserializacje z podanego pliku
     */
    public void onDeserialize(){
        Serialization serialization = new Serialization();
        serialization.deserialize(loadingFileName.getText());
    }

}
