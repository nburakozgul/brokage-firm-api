package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.mapper.OrderMapper;
import com.ing.brokagefirm.model.OrderResponse;
import com.ing.brokagefirm.model.OrderRequest;
import com.ing.brokagefirm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //TODO use custom request response models(not entity classes)

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable(value = "orderId") Long orderId) throws ResourceNotFoundException {
        Order order = orderService
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + orderId));
        return ResponseEntity.ok().body(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrderByCustomerId(@RequestParam(value = "customerId") String customerId) throws ResourceNotFoundException {
        Optional<List<Order>> orders = orderService
                .findByCustomerId(customerId);
        if (orders.isPresent() && orders.get().isEmpty()) {
            throw new ResourceNotFoundException("Orders not found for customer id: " + customerId);
        }
        return ResponseEntity.ok().body(orders.get());
    }

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) throws CustomException {
        OrderResponse orderDB;
        try{
            orderDB = orderService.createOrder(OrderMapper.INSTANCE.orderRequestToOrder(orderRequest));
        }catch (Exception e){
            throw new CustomException("Create process failed error message : " + e.getMessage());
        }

        return ResponseEntity.ok().body(orderDB);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity cancelOrder(@PathVariable(value = "orderId") Long orderId) throws ResourceNotFoundException {
        try {
            orderService.cancelById(orderId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Order not found. Error message: " + e.getMessage());
        }

        return ResponseEntity.ok().body("Order cancelled");
    }
}
