package stockExchangeApp.market;

import stockExchangeApp.Economy;
import stockExchangeApp.asset.AssetInfo;
import stockExchangeApp.trader.Fund;
import stockExchangeApp.trader.Trader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Maciej on 07.01.2018.
 */
public class FundUnitsMarket implements Serializable {
    private int id;

    private ArrayList<Boolean> isFundUnitAvailable;

    public FundUnitsMarket(int id) {
        this.id = id;
        isFundUnitAvailable = new ArrayList<>();
    }

    /**
     * Przekazuje zadanie kupna do funduszu
     * @param moneyToSpend budzet do wydania
     * @return dane transakcji
     */
    public AssetInfo buyUnits(float moneyToSpend){
        int indexOfFund = Economy.Rndm.getRandomFund();
        Fund fund = Economy.DB.getFunds().get(indexOfFund);
        AssetInfo assetInfo;
        synchronized (fund){
                assetInfo = fund.investorBuysUnits(moneyToSpend);
            }
        return assetInfo;
    }

    /**
     * Przekazuje sprzedaz fo funduszu
     * @param trader kupujacy
     */
    public void sellUnits(Trader trader){
        synchronized (trader){
            if(trader.getWallet().getAssets().size() > 0) {
                AssetInfo assetInfo = trader.getWallet().getAssets().get(Economy.Rndm.getRandomAsset(trader.getWallet()));
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Boolean> getIsFundUnitAvailable() {
        return isFundUnitAvailable;
    }

    public void setIsFundUnitAvailable(ArrayList<Boolean> isFundUnitAvailable) {
        this.isFundUnitAvailable = isFundUnitAvailable;
    }
}
