package com.ing.brokagefirm.model;

public class AssetResponse {
    private String customerId;
    private String assetId;
    private String assetName;
    private String assetDescription;
    private Double size;
    private Double usableSize;

    public AssetResponse() {
    }

    public AssetResponse(String customerId, String assetId, String assetName, String assetDescription, Double size, Double usableSize) {
        this.customerId = customerId;
        this.assetId = assetId;
        this.assetName = assetName;
        this.assetDescription = assetDescription;
        this.size = size;
        this.usableSize = usableSize;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Double getUsableSize() {
        return usableSize;
    }

    public void setUsableSize(Double usableSize) {
        this.usableSize = usableSize;
    }
}
