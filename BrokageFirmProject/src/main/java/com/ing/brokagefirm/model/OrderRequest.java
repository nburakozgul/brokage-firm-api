package com.ing.brokagefirm.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {
    @NotNull(message = "Customer ID cannot be null")
    private String customerId;
    @NotNull(message = "Order side cannot be null")
    private OrderSide side;
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    @NotNull(message = "Size cannot be null")
    @Positive(message = "Size must be greater than  0")
    private Double size;
    private String assetId;
    @NotNull(message = "Asset name cannot be null")
    private String assetName;

    public OrderRequest() {
    }

    public OrderRequest(String customerId, OrderSide side, Double price, Double size, String assetId, String assetName) {
        this.customerId = customerId;
        this.side = side;
        this.price = price;
        this.size = size;
        this.assetId = assetId;
        this.assetName = assetName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
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
}
