package stockExchangeApp.asset;

import stockExchangeApp.market.StockExchange;
import stockExchangeApp.market.stockExchange.Company;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;

public class Share extends Asset implements Serializable{
    private Company company;
    private int sharesAvailable;

    public Share(int id, String name, int sharesAvailable, Company company, float price) {
        super(id, name);
        this.sharesAvailable = sharesAvailable;
        this.company = company;
        this.currentPrice = price;
        prices = new ArrayList<>();
        prices.add(price);
        dates.add(0);
    }

    /**
     * Oddelegowuje operacji sprzedazy na matczyny rynek
     * @param trader zleceniodawca
     * @param amount ilosc sprzedawanego assetu
     */
    @Override
    public void sell(Trader trader, float amount){
        StockExchange stockExchange = company.getStockExchange();

        stockExchange.sellAsset(trader, amount, stockExchange.getCompanies().indexOf(company));
    }

    /**
     * Zmiana kursu na koniec dnia ze zmiana najwyzszego i najnizszego kursu
     * @param day numer dnia
     */
    public void changePrice(int day){
        super.changePrice(day);
        company.setCurrentExchangeRate(currentPrice);
        if(currentPrice > company.getMaxExchangeRate()){
            company.setMaxEchangeRate(currentPrice);
        }
        else if(currentPrice < company.getMinExchangeRate()){
            company.setMinExchaneRate(currentPrice);
        }
    }

    public ArrayList<Float> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<Float> prices) {
        this.prices = prices;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company Company) {
        this.company = company;
    }

    public int getSharesAvailable() {
        return sharesAvailable;
    }

    public void setSharesAvailable(int sharesAvailable) {
        this.sharesAvailable = sharesAvailable;
    }

}
