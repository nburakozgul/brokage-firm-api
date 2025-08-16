package com.ing.brokagefirm.entity;

import com.ing.brokagefirm.model.OrderSide;
import com.ing.brokagefirm.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "'order'")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
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
}
