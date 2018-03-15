package stockExchangeApp.trader;

import stockExchangeApp.Economy;
import stockExchangeApp.Randomizer;
import stockExchangeApp.asset.AssetInfo;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Trader implements Serializable{

    Wallet wallet;

    private int id;
    private float budget;
    Random random = new Random();

    public Trader(int id, float budget) {
        this.id = id;
        this.budget = budget;
    }

    /**
     * Zmniejsza budzet po zakupieniu assetu
     * @param amount koszt zakupionego assetu
     */
    public synchronized void payForAsset(float amount){
//        System.out.println("Przed kupnem " + budget);
        budget -= amount;
//        System.out.println("Po kupnie " + budget);
    }

    /**
     * Zwieksza budzet po sprzedaniu assetu
     * @param amount ilosc o jaka ma byc zwiekszony budzet
     */
    public void receiveMoneyForAsset(float amount){
        budget += amount;
    }

    /**
     * Kupuje losowy asset z losowego rynku
     */
    private synchronized void buyRandomAsset(){
        if(budget > 0) {
            float moneyToSpend = (float) ThreadLocalRandom.current().nextDouble(0, budget);
            AssetInfo assetInfo = Economy.DB.getMarkets().get(Economy.Rndm.getRandomMarketId()).buyAsset(this, moneyToSpend);
            if(assetInfo.getAmount() > 0){
                wallet.add(assetInfo);
            }
        }
    }

    /**
     * Sprzedaje losowo wybrany asset z portfela
     */
    private synchronized void sellRandomAsset(){
        if(wallet.getAssets().size() > 0) {
            AssetInfo asset = wallet.getAssets().get(Economy.Rndm.getRandomAsset(wallet));
            asset.getAsset().sell(this, asset.getAmount());
            wallet.remove(asset);
        }
    }

    /**
     * losowo generuje operacje albo kupna albo sprzedazy
     */
    void buyOrSell(){
        Random random = new Random();
        int choice = random.nextInt(2);
        switch (choice) {
            case 0:
                buyRandomAsset();
                break;
            case 1:
                sellRandomAsset();
                break;
        }
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
