package com.ing.brokagefirm.utility;

import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderHelper {
    public static void setOrderDefaultValues(Order order) {
        order.setCreatedBy(order.getCustomerId());
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderId(UUID.randomUUID().toString());
    }

    public static void updateOrder(Order order) {
        order.setUpdatedBy(order.getCustomerId());
        order.setUpdatedAt(LocalDateTime.now());
    }
}
