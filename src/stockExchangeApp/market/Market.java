package stockExchangeApp.market;

import stockExchangeApp.asset.AssetInfo;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;

public abstract class Market implements Serializable{

    private int id;
    private String name;
    private float margin;
    private boolean emptyMarket;

    public Market(int id, String name, float margin){
        this.id = id;
        this.name = name;
        this.margin = margin;
        emptyMarket = true;
    }

    /**
     * Abstrakcyjna metoda konca dnia implementowana w klasach dziedziczacych po tej klasie
     * @param day numer dnia
     */
    public abstract void dayEnded(int day);

    /**
     * Abstrakcyjna metoda pobierania marzy implementowana w klasach dziedziczacych po tej klasie
     * @param moneyToPay wartosc transakcji
     */
    void collectMargin(float moneyToPay){
        float collectedMoney = margin/100 * moneyToPay;
    }

    /**
     * Abstrakcyjna metoda sprzedazy assetu implementowana w klasach dziedziczacych po tej klasie
     * @param trader zleceniodawca
     * @param amountToBeSold ilosc sprzedawanej rzeczy
     * @param indexOfAsset indeks assetu
     */
    public abstract void sellAsset(Trader trader, float amountToBeSold, int indexOfAsset);

    /**
     * Abstrakcyjna metoda kupna assetu implementowana w klasach dziedziczacych po tej klasie
     * @param trader zleceniodawca
     * @param moneyToSpend budzet transakcji
     * @return informacje o transkakcji
     */
    public abstract AssetInfo buyAsset(Trader trader, float moneyToSpend);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public boolean isEmptyMarket() {
        return emptyMarket;
    }

    public void setEmptyMarket(boolean emptyMarket) {
        this.emptyMarket = emptyMarket;
    }
}