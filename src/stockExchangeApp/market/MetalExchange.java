package stockExchangeApp.market;

import stockExchangeApp.Economy;
import stockExchangeApp.asset.AssetInfo;
import stockExchangeApp.asset.Metal;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;

public class MetalExchange extends Market implements Serializable{
    private ArrayList<Metal> metalsList;

    public MetalExchange(int id, String name, float margin, ArrayList<Metal> metalsList) {
        super(id, name, margin);
        this.metalsList = metalsList;
    }

    /**
     * Sygnalizuje koniec dnia, odpala zmiany kursow metali
     * @param day numer dnia
     */
    @Override
    public void dayEnded(int day){
        for(Metal metal : metalsList){
            metal.changePrice(day);
        }
    }


    /**
     * Przeprowadza operacje kupna zlecona przez Inwestora lub Fundusz
     * @param trader zleceniedowca operacji kupna
     * @param moneyToSpend budzet jaki zleceniodawca przeznacza na transkacje
     * @return dane transakcji tj. co zostalo kupione i w jakiej ilosci
     */
    @Override
    public AssetInfo buyAsset(Trader trader, float moneyToSpend) {
        Metal metal = metalsList.get(Economy.Rndm.getRandomMetal(this));

        synchronized (metal) {

            float finalAmount = (float) Math.floor(moneyToSpend / metal.getCurrentPrice());
            float moneyToPay = finalAmount * metal.getCurrentPrice();
            trader.payForAsset(moneyToPay);
            metal.bought(trader.getId());

            return new AssetInfo(metal, finalAmount);
        }
    }

    /**
     * Przeprowadza operacje sprzedazy zlecona przez Inwestora lub Fundusz
     * @param trader zleceniodawca operacji sprzedazy
     * @param amount ilosc sprzedawanej rzeczy
     * @param indexOfMetal index surowca ktory bedzie sprzedawany
     */
    @Override
    public void sellAsset(Trader trader, float amount, int indexOfMetal){
        Metal metal = metalsList.get(indexOfMetal);

        synchronized (metal) {

            float value = amount * metal.getCurrentPrice();
            collectMargin(value);
            trader.receiveMoneyForAsset(value);
            metal.sold(trader.getId());
        }
    }

    public MetalExchange(int id, String name, float margin) {
        super(id, name, margin);
        metalsList = new ArrayList<>();
        this.metalsList = metalsList;
    }

    public ArrayList<Metal> getMetalsList() {
        return metalsList;
    }

    public void setMetalsList(ArrayList<Metal> metalsList) {
        this.metalsList = metalsList;
    }
}
