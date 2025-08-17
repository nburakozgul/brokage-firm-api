package com.ing.brokagefirm.service;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.entity.Order;
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
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    };

    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Optional<List<Order>> findByCustomerId(String customerId){
        return orderRepository.findByCustomerId(customerId);
    };

    @Transactional
    public OrderResponse createOrder(Order order) {
        // Fetch customer's TRY asset
        Asset tryAsset = assetRepository.findByCustomerIdAndAssetId(order.getCustomerId(), "AST005")
                .orElseThrow(() -> new RuntimeException("Customer TRY asset not found"));

        Asset sellAsset = null;

        // Calculate total cost for BUY
        double totalCost = order.getSize() * order.getPrice();

        if (order.getOrderSide() == OrderSide.BUY) {
            if (tryAsset.getUsableSize() < totalCost) {
                throw new RuntimeException("Insufficient TRY balance");
            }
            // Deduct TRY usable size
            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalCost);
            assetRepository.save(tryAsset);
            //order.setAsset(tryAsset);
        }

        if (order.getOrderSide() == OrderSide.SELL) {
            // Fetch the asset to buy/sell
            sellAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                    .orElseThrow(() -> new RuntimeException("Asset not found for customer"));

            if (sellAsset.getUsableSize() < order.getSize()) {
                throw new RuntimeException("Insufficient asset balance");
            }
            // Deduct asset usable size
            sellAsset.setUsableSize(sellAsset.getUsableSize() - order.getSize());
            assetRepository.save(sellAsset);
        }

        setOrderDefaultValues(order);
        orderRepository.save(order);

        // Create new Order
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCustomerId(order.getCustomerId());
        orderResponse.setAssetName(order.getAssetName());
        orderResponse.setOrderSide(order.getOrderSide());
        orderResponse.setOrderStatus(OrderStatus.PENDING);
        orderResponse.setPrice(order.getPrice());
        orderResponse.setSize(order.getSize());

        return orderResponse;
    }


    private void setOrderDefaultValues(Order order) {
        order.setCreatedBy(order.getCustomerId());
        order.setCreatedAt(LocalDateTime.now());
    }

    //TODO Impl Cancel a pending order. Other status orders cannot be deleted
    public void cancelById(Long id){
        orderRepository.deleteById(id);
    }
}
