package stockExchangeApp.trader;

import stockExchangeApp.asset.AssetInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class InvestorWallet extends Wallet implements Serializable{
    private ArrayList<AssetInfo> assetsManagedByFunds;

    public InvestorWallet(Trader trader) {
        super(trader);
        assetsManagedByFunds = new ArrayList<>();
    }

    public ArrayList<AssetInfo> getAssetsManagedByFunds() {
        return assetsManagedByFunds;
    }

    public void setAssetsManagedByFunds(ArrayList<AssetInfo> assetsManagedByFunds) {
        this.assetsManagedByFunds = assetsManagedByFunds;
    }
}
