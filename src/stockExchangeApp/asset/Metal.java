package stockExchangeApp.asset;

import stockExchangeApp.market.MetalExchange;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;

public class Metal extends Asset implements Serializable{

    private String tradeUnit;
    private float minPrice;
    private float maxPrice;
    private MetalExchange metalMarket;

    public Metal(int id, String name, String tradeUnit,
                 float currentPrice, float minPrice, float maxPrice, MetalExchange metalMarket) {
        super(id, name);
        this.tradeUnit = tradeUnit;
        this.currentPrice = currentPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.metalMarket = metalMarket;
        prices = new ArrayList<>();
        prices.add(currentPrice);
        dates.add(0);
    }

    /**
     * Zmiana kursu na koniec dnia ze zmiana minimalnego i maksymalnego kursu
     * @param day numer dnia
     */
    public void changePrice(int day){
        super.changePrice(day);
        if(currentPrice > maxPrice){
            maxPrice = currentPrice;
        }
        else if(currentPrice < minPrice){
            minPrice = currentPrice;
        }
    }

    /**
     * Oddelegowuje operacji sprzedazy na matczyny rynek
     * @param trader zleceniodawca
     * @param amount ilosc sprzedawanego assetu
     */
    @Override
    public void sell(Trader trader, float amount){
        metalMarket.sellAsset(trader, amount, metalMarket.getMetalsList().indexOf(this));
    }

    public ArrayList<Float> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<Float> prices) {
        this.prices = prices;
    }

    public String getTradeUnit() {
        return tradeUnit;
    }

    public void setTradeUnit(String tradeUnit) {
        this.tradeUnit = tradeUnit;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

}
