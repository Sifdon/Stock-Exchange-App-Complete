package stockExchangeApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import stockExchangeApp.asset.*;
import stockExchangeApp.asset.Currency;
import stockExchangeApp.controllers.forms.MetalMarketForm;
import stockExchangeApp.market.*;
import stockExchangeApp.market.stockExchange.Company;
import stockExchangeApp.trader.Fund;
import stockExchangeApp.trader.Investor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStorage implements Serializable{

    private int id;

    private ForeignExchange foreignExchange;
    private FundUnitsMarket fundUnitsMarket;

    //Obiekty świata
    private ArrayList<Currency> currencies;
    private ArrayList<StockExchange> stockExchanges;
    private ArrayList<MetalExchange> metalExchanges;
    private ArrayList<Market> markets;
    private List<String> countries;
    private ArrayList<Investor> investors;
    private ArrayList<Fund> funds;
    private transient ArrayList<Thread> threads;
    private volatile boolean keepRunning;

    //Aktywa do wyświetlania w oknie aplikacji
    private transient ObservableList<Share> shares = FXCollections.observableArrayList();
    private transient ObservableList<Metal> metals = FXCollections.observableArrayList();
    private transient ObservableList<stockExchangeApp.asset.Currency> currenciesObserv = FXCollections.observableArrayList();
    private transient ObservableList<Investor> investorObservableList = FXCollections.observableArrayList();
    private transient ObservableList<Fund> fundsObservableList = FXCollections.observableArrayList();
    private transient ObservableList<Company> companiesObservableList = FXCollections.observableArrayList();

    //Dane do tworzenia wykresów wartości aktywów
    private transient String chartName;
    private transient ArrayList<Integer> chartXValues;
    private transient ArrayList<Float> chartYValues;

    private transient boolean multiChartModeOn;
    private transient ArrayList<ArrayList<Integer>> chartsXValues;
    private transient ArrayList<ArrayList<Float>> chartsYValues;
    private transient ArrayList<String> seriesNames;

    private transient int idOfCompanyToDisplay;
    private transient int idOfInvestorToDisplay;
    private transient int idOfFundToDisplay;

    private int day;

    public synchronized int getId() {
        id += 1;
        return id;
    }

    DataStorage(){
        init();
    }

    /**
     * Inicjaizuje listy obiektow swiata
     */
       private void init() {
        currencies = new ArrayList<>();
        stockExchanges = new ArrayList<>();
        metalExchanges = new ArrayList<>();
        markets = new ArrayList<>();

        threads = new ArrayList<>();
        keepRunning = false;
        fundUnitsMarket = new FundUnitsMarket(getId());

        investors = new ArrayList<>();
        funds = new ArrayList<>();

        multiChartModeOn = false;
        chartsXValues = new ArrayList<>();
        chartsYValues = new ArrayList<>();
        seriesNames = new ArrayList<>();

        initCountries();
    }

    /**
     * inicjalizuje liste krajow
     */
    private void initCountries() {
        countries = new ArrayList<>();
        InputStream is = getClass().getResourceAsStream("data/countries.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        try {
            while ((line = br.readLine()) != null)
            {
                countries.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Ustawia rynek walut
     * @param newForeignExchange rynek walut
     */
    public void addForeignExchange(ForeignExchange newForeignExchange){
        foreignExchange = newForeignExchange;
        addMarket(foreignExchange);
    }

    /**
     * Dodaje gielde do "bazy danych"
     * @param stockExchange gielda
     */
    public void addStockExchange(StockExchange stockExchange){
        stockExchanges.add(stockExchange);
        addMarket(stockExchange);
    }

    /**
     * Dodaje rynek do listy rynkow
     * @param market rynel
     */
    public void addMarket(Market market) {
        markets.add(market);
    }

    /**
     * Dodaje walute do "bazy danych"
     * @param currency
     */
    public void addCurrency(Currency currency){
        currencies.add(currency);
        currenciesObserv.add(currency);
    }

    /**
     * Tworzy baze danych na podstawie danych uzyskanych przez deserializacje
     * @param dataStorage dane uzyskane poprzez deserializacje
     */
    public void deserializeData(DataStorage dataStorage) {
        init();

        this.fundUnitsMarket = dataStorage.fundUnitsMarket;
        this.currencies = dataStorage.currencies;
        this.stockExchanges = dataStorage.stockExchanges;
        this.metalExchanges = dataStorage.metalExchanges;
        this.markets = dataStorage.markets;
        this.foreignExchange = dataStorage.foreignExchange;
        this.countries = dataStorage.countries;
        this.investors = dataStorage.investors;
        this.funds = dataStorage.funds;
        this.id = dataStorage.id;
        this.day = dataStorage.day;

        clearObservables();

        for (StockExchange stockExchange : stockExchanges) {
            for (Company company : stockExchange.getCompanies()) {
                companiesObservableList.add(company);
                shares.add(company.getShare());
                Thread thread = new Thread(company);
                threads.add(thread);
            }
        }

        for (MetalExchange metalExchange : metalExchanges) {
            for (Metal metal : metalExchange.getMetalsList()) {
                metals.add(metal);
            }
        }

        currenciesObserv.addAll(currencies);

        for(Fund fund : funds){
            fundsObservableList.add(fund);
            Thread thread = new Thread(fund);
            threads.add(thread);
        }

        for(Investor investor : investors){
            investorObservableList.add(investor);
            Thread thread = new Thread(investor);
            threads.add(thread);
        }

    }

    /**
     * Czysci listy sluzace do wyswietlania obiektow w oknie aplikacji
     */
    private void clearObservables(){
        fundsObservableList.clear();
        investorObservableList.clear();
        companiesObservableList.clear();
        shares.clear();
        metals.clear();
        currenciesObserv.clear();
    }

    /**
     * Dodaje rynek metali do "bazy danych"
     * @param metalExchange rynek metali
     */
    public void addMetalExchange(MetalExchange metalExchange){
        metalExchanges.add(metalExchange);
        addMarket(metalExchange);
    }

    /**
     * Dodaje inwestora do "bazy danych"
     * @param investor inwestor
     */
    public void addInvestor(Investor investor){
        investors.add(investor);
        investorObservableList.add(investor);
    }

    /**
     * Dodaje fundusz do "bazy danych"
     * @param fund fundusz
     */
    public void addFund(Fund fund){
        funds.add(fund);
        fundsObservableList.add(fund);
    }

    /**
     * Sygnalizuje kazdemu rynkowi ze zakonczyl sie dzien
     * @param day numer dnia
     */
    public void dayEnded(int day){
        for(Market market : markets){
            market.dayEnded(day);
        }
    }

    public static void setId(int id) {
        Economy.DB.id = id;
    }

    public ForeignExchange getForeignExchange() {
        return foreignExchange;
    }

    public void setForeignExchange(ForeignExchange foreignExchange) {
        this.foreignExchange = foreignExchange;
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }

    public ArrayList<StockExchange> getStockExchanges() {
        return stockExchanges;
    }

    public void setStockExchanges(ArrayList<StockExchange> stockExchanges) {
        this.stockExchanges = stockExchanges;
    }

    public ArrayList<MetalExchange> getMetalExchanges() {
        return metalExchanges;
    }

    public void setMetalExchanges(ArrayList<MetalExchange> metalExchanges) {
        this.metalExchanges = metalExchanges;
    }

    public ArrayList<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<Market> markets) {
        this.markets = markets;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCoun(List<String> countries) {
        this.countries = countries;
    }

    public ArrayList<Investor> getInvestors() {
        return investors;
    }

    public void setInvestors(ArrayList<Investor> investors) {
        this.investors = investors;
    }

    public ArrayList<Fund> getFunds() {
        return funds;
    }

    public void setFunds(ArrayList<Fund> funds) {
        this.funds = funds;
    }

    public ArrayList<Thread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<Thread> threads) {
        this.threads = threads;
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }

    public ObservableList<Share> getShares() {
        return shares;
    }

    public void setShares(ObservableList<Share> shares) {
        this.shares = shares;
    }

    public ObservableList<Metal> getMetals() {
        return metals;
    }

    public void setMetals(ObservableList<Metal> metals) {
        this.metals = metals;
    }

    public ObservableList<Currency> getCurrenciesObserv() {
        return currenciesObserv;
    }

    public void setCurrenciesObserv(ObservableList<Currency> currenciesObserv) {
        this.currenciesObserv = currenciesObserv;
    }

    public ObservableList<Investor> getInvestorObservableList() {
        return investorObservableList;
    }

    public void setInvestorObservableList(ObservableList<Investor> investorObservableList) {
        this.investorObservableList = investorObservableList;
    }

    public ObservableList<Fund> getFundsObservableList() {
        return fundsObservableList;
    }

    public void setFundsObservableList(ObservableList<Fund> fundsObservableList) {
        this.fundsObservableList = fundsObservableList;
    }

    public ObservableList<Company> getCompaniesObservableList() {
        return companiesObservableList;
    }

    public void setCompaniesObservableList(ObservableList<Company> companiesObservableList) {
        this.companiesObservableList = companiesObservableList;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public ArrayList<Integer> getChartXValues() {
        return chartXValues;
    }

    public void setChartXValues(ArrayList<Integer> chartXValues) {
        this.chartXValues = chartXValues;
    }

    public ArrayList<Float> getChartYValues() {
        return chartYValues;
    }

    public void setChartYValues(ArrayList<Float> chartYValues) {
        this.chartYValues = chartYValues;
    }

    public boolean isMultiChartModeOn() {
        return multiChartModeOn;
    }

    public void setMultiChartModeOn(boolean multiChartModeOn) {
        this.multiChartModeOn = multiChartModeOn;
    }

    public ArrayList<ArrayList<Integer>> getChartsXValues() {
        return chartsXValues;
    }

    public void setChartsXValues(ArrayList<ArrayList<Integer>> chartsXValues) {
        this.chartsXValues = chartsXValues;
    }

    public ArrayList<ArrayList<Float>> getChartsYValues() {
        return chartsYValues;
    }

    public void setChartsYValues(ArrayList<ArrayList<Float>> chartsYValues) {
        this.chartsYValues = chartsYValues;
    }

    public ArrayList<String> getSeriesNames() {
        return seriesNames;
    }

    public void setSeriesNames(ArrayList<String> seriesNames) {
        this.seriesNames = seriesNames;
    }

    public int getIdOfCompanyToDisplay() {
        return idOfCompanyToDisplay;
    }

    public void setIdOfCompanyToDisplay(int idOfCompanyToDisplay) {
        this.idOfCompanyToDisplay = idOfCompanyToDisplay;
    }

    public int getIdOfInvestorToDisplay() {
        return idOfInvestorToDisplay;
    }

    public void setIdOfInvestorToDisplay(int idOfInvestorToDisplay) {
        this.idOfInvestorToDisplay = idOfInvestorToDisplay;
    }

    public int getIdOfFundToDisplay() {
        return idOfFundToDisplay;
    }

    public void setIdOfFundToDisplay(int idOfFundToDisplay) {
        this.idOfFundToDisplay = idOfFundToDisplay;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public FundUnitsMarket getFundUnitsMarket() {
        return fundUnitsMarket;
    }

    public void setFundUnitsMarket(FundUnitsMarket fundUnitsMarket) {
        this.fundUnitsMarket = fundUnitsMarket;
    }
}
