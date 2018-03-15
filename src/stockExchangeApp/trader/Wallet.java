package stockExchangeApp.trader;


import stockExchangeApp.asset.Asset;
import stockExchangeApp.asset.AssetInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Wallet implements Serializable {
    private ArrayList<AssetInfo> assets;
    Trader trader;

    Wallet(Trader trader) {
        assets = new ArrayList<>();
        this.trader = trader;
    }

    /**
     * Dodaje asset do portfela, jezeli juz wystepuje, to zwieksza jego ilosc, wpp dodaje jako nowy
     * @param assetInfo
     */
    public void add(AssetInfo assetInfo) {
        boolean assetAlreadyInWallet = false;

        for(AssetInfo asset : assets){
            if(asset.getAsset().equals(assetInfo.getAsset())){
                asset.increaseAmount(assetInfo.getAmount());
                assetAlreadyInWallet = true;
            }
        }

        if(!assetAlreadyInWallet){
            assets.add(assetInfo);
        }
    }

    /**
     * Usuwa asset z listy assetow
     * @param asset Asset do usuniecia
     */
    void remove(AssetInfo asset){
        assets.remove(asset);
    }

    /**
     * Wyswietla zawartosc portfela
     */
    void showAssets(){
        System.out.println("Stan portfela tradera o id: " + trader.getId());
        for(AssetInfo asset : assets){
            System.out.println(asset.getAsset().getName() + " : " + asset.getAmount());
        }
        System.out.println("--------------");
    }

    /**
     * Zwraca indeks assetu z lity assetow
     * @param asset szukany asset
     * @return index assetu
     */
    public int indexOfAsset(Asset asset){
        int assetBeingSearched = 0;
        for(AssetInfo assetInfo : assets){
            if(assetInfo.getAsset().equals(asset)){
                assetBeingSearched  = assets.indexOf(assetInfo);
            }
        }
        return assetBeingSearched;
    }

    public ArrayList<AssetInfo> getAssets() {
        return assets;
    }

    public void setAssets(ArrayList<AssetInfo> assets) {
        this.assets = assets;
    }
}
