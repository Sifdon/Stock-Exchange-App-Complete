package stockExchangeApp.asset;

import java.io.Serializable;

/**
 * Created by Maciej on 05.01.2018.
 */
public class AssetInfo implements Serializable{
    private Asset asset;
    private float amount;

    public AssetInfo(Asset asset, float amount) {
        this.asset = asset;
        this.amount = amount;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void increaseAmount(float amount){
        this.amount += amount;
    }

    public void decreaseAmount(float amount){
        this.amount -= amount;
    }


}
