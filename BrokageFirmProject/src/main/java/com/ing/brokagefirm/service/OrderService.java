package com.ing.brokagefirm.service;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.mapper.OrderMapper;
import com.ing.brokagefirm.model.OrderRequest;
import com.ing.brokagefirm.model.OrderResponse;
import com.ing.brokagefirm.model.OrderSide;
import com.ing.brokagefirm.model.OrderStatus;
import com.ing.brokagefirm.repository.AssetRepository;
import com.ing.brokagefirm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    public OrderResponse findById(Long id){
        return orderRepository.findById(id).map(OrderMapper.INSTANCE::orderToOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + id));
    };

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public List<OrderResponse> findByCustomerId(String customerId){
        List<OrderResponse> orders = orderRepository.findByCustomerId(customerId).stream()
                .map(OrderMapper.INSTANCE::orderToOrderResponse)
                .toList();

        if (Objects.isNull(orders) || orders.isEmpty()) {
            throw new ResourceNotFoundException("Orders not found for customer id: " + customerId);
        }

        return orders;
    };

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = OrderMapper.INSTANCE.orderRequestToOrder(orderRequest);
        // Fetch customer's TRY asset
        Asset tryAsset = assetRepository.findByCustomerIdAndAssetId(order.getCustomerId(), "AST005")
                .orElseThrow(() -> new RuntimeException("Customer TRY asset not found"));

        // Calculate total cost for BUY
        double totalCost = order.getSize() * order.getPrice();

        if (order.getSide() == OrderSide.BUY) {
            if (tryAsset.getUsableSize() < totalCost) {
                throw new CustomException("Insufficient TRY balance");
            }
            // Deduct TRY usable size
            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalCost);
            assetRepository.save(tryAsset);
        }

        Asset sellAsset = null;
        if (order.getSide() == OrderSide.SELL) {
            // Fetch the asset to buy/sell
            sellAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                    .orElseThrow(() -> new CustomException("Asset not found for customer"));

            if (sellAsset.getUsableSize() < order.getSize()) {
                throw new CustomException("Insufficient asset balance");
            }
            // Deduct asset usable size
            sellAsset.setUsableSize(sellAsset.getUsableSize() - order.getSize());
            assetRepository.save(sellAsset);
            order.setAsset(sellAsset);
        }

        setOrderDefaultValues(order);
        Order orderDB = orderRepository.save(order);
        return OrderMapper.INSTANCE.orderToOrderResponse(orderDB);
    }

    public void cancelById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found for id: " + id));
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    // TODO Carry this to utility class?
    private void setOrderDefaultValues(Order order) {
        order.setCreatedBy(order.getCustomerId());
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderId(UUID.randomUUID().toString());
    }
}
