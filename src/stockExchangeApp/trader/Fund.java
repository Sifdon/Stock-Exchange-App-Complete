package stockExchangeApp.trader;

import stockExchangeApp.Economy;
import stockExchangeApp.asset.AssetInfo;


import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.Thread.sleep;

public class Fund extends Trader implements Runnable, Serializable {
    private String name;
    private String managersFirstName;
    private String managersLastName;
    private boolean keepRunning;

    public Fund(int id, float budget, String name, String managersFirstName, String managersLastName) {
        super(id, budget);
        this.name = name;
        this.managersFirstName = managersFirstName;
        this.managersLastName = managersLastName;
        this.wallet = new Wallet(this);
    }

    /**
     * Petla watku w losowych odstepach generuje operacje kupna i sprzedazy
     */
    @Override
    public void run() {
        if(Economy.DB.getMarkets().size() > 0) {
            while (Economy.DB.isKeepRunning()) {
                buyOrSell();
                try {
                    sleep(random.nextInt(3000));
                } catch (InterruptedException e) {
                    System.out.println("Przerwano sen");
                     break;
                }
            }
        }
    }

    public AssetInfo investorBuysUnits(float budget){
            return Economy.DB.getMarkets().get(Economy.Rndm.getRandomMarketId()).buyAsset(this, budget);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagersFirstName() {
        return managersFirstName;
    }

    public void setManagersFirstName(String managersFirstName) {
        this.managersFirstName = managersFirstName;
    }

    public String getManagersLastName() {
        return managersLastName;
    }

    public void setManagersLastName(String managersLastName) {
        this.managersLastName = managersLastName;
    }
}
