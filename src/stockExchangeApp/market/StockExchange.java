package stockExchangeApp.market;


import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.asset.Asset;
import stockExchangeApp.asset.AssetInfo;
import stockExchangeApp.asset.Currency;
import stockExchangeApp.asset.Share;
import stockExchangeApp.market.stockExchange.Company;
import stockExchangeApp.market.stockExchange.Index;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StockExchange extends Market implements Serializable{
    private String country;
    private String city;
    private String address;
    private ArrayList<Index> indexes;
    private ArrayList<Company> companies;


    public StockExchange(int id, float margin, String name, String country,
                         String city, String address, ArrayList<Index> indexes) {
        super(id, name, margin);
        this.country = country;
        this.city = city;
        this.address = address;
        this.indexes = indexes;
    }

    public StockExchange(int id, float margin, String name, String country, String city, String address) {
        super(id, name, margin);
        this.country = country;
        this.city = city;
        this.address = address;
        companies = new ArrayList<>();
        indexes = new ArrayList<>();
    }

    /**
     * Na koniec dnia odpala zmiany kursow dla spolek
     * @param day numer dnia
     */
    @Override
    public void dayEnded(int day){
        for(Company company : companies){
            company.getShare().changePrice(day);
        }
    }


    /**
     * Przeprowadza operacje kupna zlecona przez Inwestora lub Fundusz
     * @param trader zleceniedowca operacji kupna
     * @param moneyToSpend budzet jaki zleceniodawca przeznacza na transkacje
     * @return dane transakcji tj. co zostalo kupione i w jakiej ilosci
     */
    @Override
    public AssetInfo buyAsset(Trader trader, float moneyToSpend){
        Company company = companies.get(Economy.Rndm.getRandomCompany(this));

        synchronized (company) {

            int sharesAvailable = company.getNumberOfshares();
            int canBuy = (int) Math.floor(moneyToSpend / company.getCurrentExchangeRate());
            int finalAmount = Math.min(sharesAvailable, canBuy);
            float moneyToPay = finalAmount * company.getCurrentExchangeRate();
            company.setNumberOfshares(company.getNumberOfshares() - finalAmount);
            company.getShare().setSharesAvailable(company.getShare().getSharesAvailable() - finalAmount);
            company.buyingShares(trader);

            company.getShare().bought(trader.getId());

            trader.payForAsset(moneyToPay);
            collectMargin(moneyToPay);

            return new AssetInfo(company.getShare(), finalAmount);
        }
    }

    /**
     * Przeprowadza operacje sprzedazy zlecona przez Inwestora lub Fundusz
     * @param trader zleceniodawca operacji sprzedazy
     * @param amount ilosc sprzedawanej rzeczy
     * @param indexOfCompany index spolki ktorej akcje beda sprzedawane
     */
    @Override
    public void sellAsset(Trader trader, float amount, int indexOfCompany){
        Company company = companies.get(indexOfCompany);

        synchronized (company) {

            float value = amount * company.getCurrentExchangeRate();
            collectMargin(value);
            company.sellingShares(trader);
            trader.receiveMoneyForAsset(value);
            company.getShare().sold(trader.getId());
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(ArrayList<Index> indexes) {
        this.indexes = indexes;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }
}
