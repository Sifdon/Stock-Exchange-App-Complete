package stockExchangeApp.asset;

import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class Asset implements Serializable{
    private int id;
    private String name;
    float currentPrice;
    private HashSet<Integer> buyers;
    private HashSet<Integer> sellers;
    ArrayList<Integer> dates;
    ArrayList<Float> prices;
    ArrayList<Float> percentageChanges;


    public Asset(int id, String name) {
        this.id = id;
        this.name = name;
        buyers = new HashSet<>();
        sellers = new HashSet<>();
        dates = new ArrayList<>();
        percentageChanges =  new ArrayList<>();
    }

    /**
     * Abstrakcyjna metoda sygnalizujaza chec sprzedazy danego assetu
     * @param trader zleceniodawca
     * @param amount ilosc sprzedawanego assetu
     */
    public abstract void sell(Trader trader, float amount);

    /**
     * Dodaje id kupujacego akcje do setu kupujacych
     * @param traderID id kupujacego
     */
    public void bought(int traderID){
        buyers.add(traderID);
    }

    /**
     * Dodaje id sprzedajacego akcje do setu sprzedajacych
     * @param traderID
     */
    public void sold(int traderID){
        sellers.add(traderID);
    }

    /**
     * Oblicza zmiane kursu, zapisuje ja do listy zmian kursu
     * @return zmiana kursu
     */
    private float endOfTheDay(){
        float change = 1;
        if(buyers.size() > 0 && sellers.size() > 0){
           change = (float) buyers.size() / sellers.size();
           if(change > 1){
               change = 1 + change * 0.25f;
           }
        }
        modifyPercentChanges(change);
        buyers.clear();
        sellers.clear();
        return change;
    }

    /**
     * Dodaje do listy procentowych zmian kursow
     * @param change zmiana
     */
    private void modifyPercentChanges(float change){
        if(change > 1){
            percentageChanges.add(change * 100);
        }
        else if(change == 1){
            percentageChanges.add(0.f);
        }
        else{
            percentageChanges.add((1 - change) * 100);
        }
    }

    /**
     * Modyfikuje cene assetu
     * @param day numer dnia
     */
    public void changePrice(int day){
        float change = endOfTheDay();
        currentPrice *= change;
        prices.add(currentPrice);
        dates.add(day);
    }

    public HashSet<Integer> getBuyers() {
        return buyers;
    }

    public void setBuyers(HashSet<Integer> buyers) {
        this.buyers = buyers;
    }

    public HashSet<Integer> getSellers() {
        return sellers;
    }

    public void setSellers(HashSet<Integer> sellers) {
        this.sellers = sellers;
    }

    public ArrayList<Integer> getDates() {
        return dates;
    }

    public void setDates(ArrayList<Integer> dates) {
        this.dates = dates;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

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

    public ArrayList<Float> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<Float> prices) {
        this.prices = prices;
    }

    public ArrayList<Float> getPercentageChanges() {
        return percentageChanges;
    }

    public void setPercentageChanges(ArrayList<Float> percentageChanges) {
        this.percentageChanges = percentageChanges;
    }
}
