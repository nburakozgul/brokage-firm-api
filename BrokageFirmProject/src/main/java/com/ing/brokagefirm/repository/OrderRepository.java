package com.ing.brokagefirm.repository;

import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("""
    SELECT o FROM Order o
    WHERE o.customerId = :customerId
      AND (:startDate IS NULL OR o.createdAt >= :startDate)
      AND (:endDate IS NULL OR o.createdAt <= :endDate)
""")
    List<Order> findByCustomerId(
            @Param("customerId") String customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Order> findOrdersByOrderStatus(OrderStatus orderStatus);
}
