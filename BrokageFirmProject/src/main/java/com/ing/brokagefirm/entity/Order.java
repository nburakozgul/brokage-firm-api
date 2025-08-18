package com.ing.brokagefirm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ing.brokagefirm.model.OrderSide;
import com.ing.brokagefirm.model.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "'order'")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    private Long id;
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    @Column(name = "asset_name", nullable = false)
    private String assetName;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_side", nullable = false)
    private OrderSide side;
    private Double price;
    private Double size;

    @ManyToOne
    @JoinColumn(name="asset_id", referencedColumnName = "id")
    @JsonBackReference
    private Asset asset;

    public Order(LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Long id, String orderId, String customerId, String assetName, OrderStatus orderStatus, OrderSide side, Double price, Double size) {
        super(createdAt, updatedAt, createdBy, updatedBy);
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.assetName = assetName;
        this.orderStatus = orderStatus;
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide orderSide) {
        this.side = orderSide;
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

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}
