package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.model.OrderRequest;
import com.ing.brokagefirm.model.OrderResponse;
import com.ing.brokagefirm.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/order")
@EnableMethodSecurity
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable(value = "orderId") @Valid Long orderId) throws ResourceNotFoundException {
        OrderResponse orderResponse = orderService.findById(orderId);
        return ResponseEntity.ok().body(orderResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderResponse>> getOrderByCustomerId(@RequestParam(value = "customerId") @Valid String customerId) throws ResourceNotFoundException {
        List<OrderResponse> orders = orderService
                .findByCustomerId(customerId);
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest) throws CustomException {
        OrderResponse orderResponse;
        try{
            orderResponse = orderService.createOrder(orderRequest);
        }catch (Exception e){
            throw new CustomException("Create process failed error message : " + e.getMessage());
        }

        return ResponseEntity.ok().body(orderResponse);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity cancelOrder(@PathVariable(value = "orderId") @Valid Long orderId) throws ResourceNotFoundException {
        try {
            orderService.cancelById(orderId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Order not found. Error message: " + e.getMessage());
        }

        return ResponseEntity.ok().body("Order cancelled");
    }
}
