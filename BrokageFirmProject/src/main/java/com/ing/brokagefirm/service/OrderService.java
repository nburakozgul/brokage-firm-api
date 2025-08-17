package com.ing.brokagefirm.service;

import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    };

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Optional<List<Order>> findByCustomerId(String customerId){
        return orderRepository.findByCustomerId(customerId);
    };


    //TODO implement order creation business logic
    public Order createOrder(Order order){
        return orderRepository.save(order);
    }

    //TODO Impl Cancel a pending order. Other status orders cannot be deleted
    public void cancelById(Long id){
        orderRepository.deleteById(id);
    }
}
