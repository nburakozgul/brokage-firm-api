package com.ing.brokagefirm.entity;

import com.ing.brokagefirm.model.OrderSide;
import com.ing.brokagefirm.model.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "'order'")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "asset_name")
    private String assetName;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_side")
    private OrderSide orderSide;
    private Double price;
    private Double size;

    public Order(LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Long id, String orderId, String customerId, String assetName, OrderStatus orderStatus, OrderSide orderSide, Double price, Double size) {
        super(createdAt, updatedAt, createdBy, updatedBy);
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.assetName = assetName;
        this.orderStatus = orderStatus;
        this.orderSide = orderSide;
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

    public OrderSide getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(OrderSide orderSide) {
        this.orderSide = orderSide;
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
}
