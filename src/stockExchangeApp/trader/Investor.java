package stockExchangeApp.trader;

import stockExchangeApp.Economy;
import stockExchangeApp.asset.AssetInfo;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;
import static stockExchangeApp.Economy.DB;

public class Investor extends Trader implements Runnable, Serializable{

    private String firstName;
    private String lastName;
    private String pesel;

    public Investor(int id, String firstName, String lastName, String pesel, float budget) {
       super(id, budget);
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.wallet = new InvestorWallet(this);
    }

    /**
     * Petla watku inwestora, w losowych momentach generuje operacje kupna i sprzedazy
     */
    @Override
    public void run() {
        if(DB.getMarkets().size() > 0) {
            while (DB.isKeepRunning()) {
               randomStuff();
                try {
                    sleep(random.nextInt(3000));
                } catch (InterruptedException e) {
                    System.out.println("Przerwano sen");
                    break;
                }
            }
        }
    }

    /**
     * W losowy sposob wybiera operacje zwieksza budzetu lub kupowania i sprzedazy
     */
    private void randomStuff(){
        int choice =  random.nextInt(2);
        switch (choice){
            case 0:
                buyOrSell();
                break;
            case 1:
               increaseBudget();
                break;
            case 2:
                buyOrSellFundUnits();
                break;
        }
    }

    private synchronized void buyOrSellFundUnits(){
        buyFundUnits();
        sellFundUnits();
    }

    private void sellFundUnits(){
        Economy.DB.getFundUnitsMarket().sellUnits(this);
    }

    private void buyFundUnits(){
        if(getBudget() > 0) {
            float moneyToSpend = (float) ThreadLocalRandom.current().nextDouble(0, getBudget());
            AssetInfo assetInfo = Economy.DB.getFundUnitsMarket().buyUnits(moneyToSpend);
        }
    }

    /**
     * Zwieksza budzet o 50%
     */
    public void increaseBudget(){
        setBudget(getBudget() * 1.5f);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        pesel = pesel;
    }

}
