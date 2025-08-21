package com.ing.brokagefirm.utility;

import com.ing.brokagefirm.entity.Asset;
import java.time.LocalDateTime;

public class AssetHelper {
    public static void updateAsset(Asset asset) {
        asset.setUpdatedBy(asset.getCustomerId());
        asset.setUpdatedAt(LocalDateTime.now());
    }

    public static void setAssetDefaultValues(Asset asset) {
        asset.setCreatedBy("system");
        asset.setCreatedAt(LocalDateTime.now());
    }

    public static Asset createAssetWithCustomerId(String customerId, Asset assetSeller) {
        Asset asset = new Asset();
        asset.setCustomerId(customerId);
        asset.setAssetName(assetSeller.getAssetName());
        asset.setAssetId(assetSeller.getAssetId());
        asset.setAssetDescription(assetSeller.getAssetDescription());
        asset.setSize(0.0);
        asset.setUsableSize(0.0);
        setAssetDefaultValues(asset);
        return asset;
    }
}
