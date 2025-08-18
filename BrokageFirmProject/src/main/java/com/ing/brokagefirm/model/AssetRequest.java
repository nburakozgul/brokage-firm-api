package com.ing.brokagefirm.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AssetRequest {
    @NotNull(message = "Customer ID cannot be null")
    private String customerId;
    @NotNull(message = "Asset ID cannot be null")
    private String assetId;
    @NotNull(message = "Asset name cannot be null")
    private String assetName;
    private String assetDescription;
    @NotNull(message = "Size cannot be null")
    @Positive(message = "Price must be greater than 0")
    private Double size;
    @NotNull(message = "Usable size cannot be null")
    @Positive(message = "Usable size must be greater than 0")
    private Double usableSize;

    public AssetRequest() {
    }

    public AssetRequest(String customerId, String assetId, String assetName, String assetDescription, Double size, Double usableSize) {
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
