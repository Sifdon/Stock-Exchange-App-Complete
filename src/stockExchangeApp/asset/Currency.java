package stockExchangeApp.asset;


import stockExchangeApp.Economy;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;

public class Currency extends Asset implements Serializable {
    private ArrayList<String> countriesUsing;

    public Currency(int id, String name, ArrayList<String> countriesUsing) {
        super(id, name);
        this.countriesUsing = countriesUsing;
        prices = new ArrayList<>();
        dates.add(0);
    }

    /**
     * Oddelegowuje operacji sprzedazy na matczyny rynek
     * @param trader zleceniodawca
     * @param amount ilosc sprzedawanego assetu
     */
    @Override
    public void sell(Trader trader, float amount) {

        Economy.DB.getForeignExchange().sellAsset(trader, amount, Economy.DB.getForeignExchange().getCurrencies().indexOf(this));
    }

    public ArrayList<String> getCountriesUsing() {
        return countriesUsing;
    }

    public void setCountriesUsing(ArrayList<String> countriesUsing) {
        this.countriesUsing = countriesUsing;
    }

    public ArrayList<Float> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<Float> prices) {
        this.prices = prices;
    }

}
