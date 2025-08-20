package com.ing.brokagefirm.utility;

import com.ing.brokagefirm.entity.Asset;
import java.time.LocalDateTime;

public class AssetHelper {
    public static void updateAsset(Asset asset) {
        asset.setUpdatedBy(asset.getCustomerId());
        asset.setUpdatedAt(LocalDateTime.now());
    }

    public static void setAssetDefaultValues(Asset asset) {
        asset.setCreatedBy(asset.getCustomerId());
        asset.setCreatedAt(LocalDateTime.now());
    }
}
