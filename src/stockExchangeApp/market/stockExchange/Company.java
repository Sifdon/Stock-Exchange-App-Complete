package stockExchangeApp.market.stockExchange;


import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.asset.Share;
import stockExchangeApp.market.StockExchange;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import static java.lang.Thread.sleep;

public class Company implements Serializable, Runnable{
    private int id;
    private String name;
    private float openingExchangeRate;
    private float currentExchangeRate;
    private float minExchangeRate;
    private float maxExchangeRate;
    private int numberOfshares;
    private float income;
    private float profit;
    private float equityCapital;
    private float shareCapital;
    private int volume;
    private float sales;
    private Share share;
    private StockExchange stockExchange;
    private HashSet<Trader> tradersWithShares;

    public Company(int id, String name, float openingExchangeRate,
                   float currentExchangeRate, float minExchangeRate, float maxExchangeRate,
                   int numberOfshares, float income, float profit, float equityCapital, float shareCapital,
                   int volume, float sales, StockExchange stockExchange) {
        this.id = id;
        this.name = name;
        this.openingExchangeRate = openingExchangeRate;
        this.currentExchangeRate = currentExchangeRate;
        this.minExchangeRate = minExchangeRate;
        this.maxExchangeRate = maxExchangeRate;
        this.numberOfshares = numberOfshares;
        this.income = income;
        this.profit = profit;
        this.equityCapital = equityCapital;
        this.shareCapital = shareCapital;
        this.volume = volume;
        this.sales = sales;
        this.share = new Share(Economy.DB.getId(), name, numberOfshares, this, currentExchangeRate);
        Economy.DB.getShares().add(share);
        this.stockExchange = stockExchange;
        tradersWithShares = new HashSet<>();
    }

    /**
     * Gdy ktos sprzedaje akcje, usuwa go z setu osob posiadajacych akcjce
     * @param trader sprzedajacy akcje
     */
    public void sellingShares(Trader trader){
        tradersWithShares.remove(trader);
    }

    /**
     * Gdy ktos kupuje akcjie, dodaje go do setu osob posiadajacych akcje
     * @param trader kupujacy akjce
     */
    public void buyingShares(Trader trader) {tradersWithShares.add(trader);}

    /**
     * Operacja skupowania akcji z rynku po okreslonej cenie
     * @param price cena wykupu
     */
    public void repurchaseShares(float price){
        if(tradersWithShares.size() > 0) {
            Trader traders[] = new Trader[tradersWithShares.size()];
            tradersWithShares.toArray(traders);
            for (Trader trader : traders) {
                int indexOfShareBeingRemoved = trader.getWallet().indexOfAsset(share);
                if(trader.getWallet().getAssets().size() > 0) {
                    float sharesToBuy = trader.getWallet().getAssets().get(indexOfShareBeingRemoved).getAmount();
                    trader.receiveMoneyForAsset(sharesToBuy * price);
                    trader.getWallet().getAssets().remove(indexOfShareBeingRemoved);
                }
            }
            tradersWithShares.clear();
        }
    }

    /**
     * Wypusczanie nowych akcji na rynek
     */
    private void releaseNewShares(){
        numberOfshares += numberOfshares * Economy.Rndm.generator.nextFloat();
    }

    /**
     * Generowanie zysku i przychodow
     */
    private void generateProfitAndIncome(){
        income = numberOfshares * currentExchangeRate * Economy.Rndm.generator.nextFloat();
        profit = income * Economy.Rndm.generator.nextFloat();
    }

    /**
     * Losowe wybieranie operacji do wykonania
     */
    private void randomStuff(){
        int choice =  Economy.Rndm.generator.nextInt(2);
        switch (choice){
            case 0:
                releaseNewShares();
                break;
            case 1:
                generateProfitAndIncome();
                break;
        }
    }

    /**
     * Petla watku realizujaca operacje wypuszczania akcji, generowania zyskow i przychodow
     */
    @Override
    public void run() {
        while (Economy.DB.isKeepRunning()) {
            randomStuff();
            try {
                sleep(Economy.Rndm.generator.nextInt(100000));
            } catch (InterruptedException e) {
                System.out.println("przerwano mi sen :<");
                break;
            }

        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StockExchange getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    public HashSet<Trader> getTradersWithShares() {
        return tradersWithShares;
    }

    public void setTradersWithShares(HashSet<Trader> tradersWithShares) {
        this.tradersWithShares = tradersWithShares;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public float getEquityCapital() {
        return equityCapital;
    }

    public void setEquityCapital(float equityCapital) {
        this.equityCapital = equityCapital;
    }

    public float getShareCapital() {
        return shareCapital;
    }

    public void setShareCapital(float shareCapital) {
        this.shareCapital = shareCapital;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getSales() {
        return sales;
    }

    public void setSales(float sales) {
        this.sales = sales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOpeningExchangeRate() {
        return openingExchangeRate;
    }

    public void setOpeningExchangeRate(float openingExchangeRate) {
        this.openingExchangeRate = openingExchangeRate;
    }

    public float getCurrentExchangeRate() {
        return currentExchangeRate;
    }

    public void setCurrentExchangeRate(float currentExchangeRate) {
        this.currentExchangeRate = currentExchangeRate;
    }

    public float getMinExchangeRate() {
        return minExchangeRate;
    }

    public void setMinExchaneRate(float minExchaneRate) {
        this.minExchangeRate = minExchangeRate;
    }

    public float getMaxExchangeRate() {return maxExchangeRate;}

    public void setMaxEchangeRate(float maxEchangeRate) {
        this.maxExchangeRate = maxExchangeRate;
    }

    public int getNumberOfshares() {
        return numberOfshares;
    }

    public void setNumberOfshares(int numberOfshares) {
        this.numberOfshares = numberOfshares;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }
}

