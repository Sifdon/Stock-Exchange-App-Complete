package stockExchangeApp.market;

import stockExchangeApp.Economy;
import stockExchangeApp.asset.AssetInfo;
import stockExchangeApp.asset.Currency;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class ForeignExchange extends Market implements Serializable{

    private ArrayList<Currency> currencies;
    private ArrayList<ArrayList<Float>> currencyPrices;
    private ArrayList<Float> zlotyToOther;
    private ArrayList<Float> otherToZloty;

    public ForeignExchange(int id, String name, float margin) {
        super(id, name, margin);
        this.currencies = Economy.DB.getCurrencies();
        if(currencies.size() > 0){
            initCurrencyPrices();
        }
    }

    /**
     * Generuje listy kursow dla walut
      */
    private void initCurrencyPrices(){
        zlotyToOther = Economy.Rndm.getRandomZlotyPrices(currencies.size());
        otherToZloty = new ArrayList<>();
        for(int i = 0; i < zlotyToOther.size(); i++){
            float price = 1.f/zlotyToOther.get(i);
            otherToZloty.add(price);
            currencies.get(i).setCurrentPrice(price);
        }
        currencyPrices = Economy.Rndm.getRandomCurrencyPrices(currencies.size());
    }

    /**
     * Dodaje nowa walute do rynku
     * @param currency waluta
     */
    public void addNewCurrency(Currency currency){
        if(currencies.size() == 1){
            initCurrencyPrices();
        }
        else if(currencies.size() > 1){
            zlotyToOther.add(Economy.Rndm.generator.nextFloat() * 10);
            float price = 1/(zlotyToOther.get(currencies.size() - 1));
            otherToZloty.add(price);
            currency.setCurrentPrice(price);
            currencyPrices = Economy.Rndm.addNewCurrencyToForex(currencyPrices);
        }
    }

    /**
     * Wyswietla listy kursow
     */
    public void showCurrencyPrices(){
        for(ArrayList<Float> x : currencyPrices){
            for(Float price : x){
                System.out.print(price + "\t");
            }
            System.out.println(" ");
        }
        for(Float x : otherToZloty){
            System.out.print(x + " ");
        }
        System.out.println("");
        for(Float x : zlotyToOther){
            System.out.print(x + " ");
        }
    }

    /**
     * Sygnalizuje koniec dnia, modyfikuje ceny walut
     * @param day numer dnia
     */
    @Override
    public void dayEnded(int day) {
        for(int i = 0; i < currencies.size(); i++){
            currencies.get(i).changePrice(day);
        }
    }


    /**
     * Losuje czy sprzedawana ma byc waluta na inna walute czy na zloty
     * @return 0 lub 1
     */
    private int exchangeTo(){
        Random random = new Random();
        return random.nextInt(2);
    }

    /**
     * Realizuje operacji sprzedazy waluty
     * @param trader zleceniodawca
     * @param amountToBeSold ilosc sprzedawanej rzeczy
     * @param idOfAsset index sprzedawanej waluty
     */
    @Override
    public synchronized void sellAsset(Trader trader, float amountToBeSold, int idOfAsset) {
        if(exchangeTo() == 0){
            float value = otherToZloty.get(idOfAsset);
            collectMargin(value);
            trader.receiveMoneyForAsset(value);
            currencies.get(idOfAsset).sold(trader.getId());
        }
        else{
            int currencyIndex = Economy.Rndm.getRandomCurrency(currencies);
            float finalAmount = amountToBeSold * currencyPrices.get(idOfAsset).get(currencyIndex);
            trader.getWallet().add(new AssetInfo(currencies.get(currencyIndex), finalAmount));
        }
    }

    /**
     * Realizuje operacje kupna waluty
     * @param trader zleceniodawca
     * @param moneyToSpend budzet transakcji
     * @return informacje o transakcji
     */
    @Override
    public synchronized AssetInfo buyAsset(Trader trader, float moneyToSpend) {
        int currencyIndex = Economy.Rndm.getRandomCurrency(currencies);

        float finalAmount = (float) Math.floor(moneyToSpend / zlotyToOther.get(currencyIndex));
        float moneyToPay = finalAmount * zlotyToOther.get(currencyIndex);
        trader.payForAsset(moneyToPay);
        Currency currency =  currencies.get(currencyIndex);
        currency.bought(trader.getId());

        return new AssetInfo(currency, finalAmount);
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }
}
