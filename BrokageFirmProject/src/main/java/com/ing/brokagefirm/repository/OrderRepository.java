package com.ing.brokagefirm.repository;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<List<Order>> findByCustomerId(String customerId);
}
