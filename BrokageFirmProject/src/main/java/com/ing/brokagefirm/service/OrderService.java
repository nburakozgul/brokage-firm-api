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
import com.ing.brokagefirm.utility.AssetHelper;
import com.ing.brokagefirm.utility.LocalDateConverter;
import com.ing.brokagefirm.utility.OrderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


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

    public List<OrderResponse> findByCustomerId(String customerId, LocalDate startDate, LocalDate endDate) throws  ResourceNotFoundException {
        List<OrderResponse> orders = orderRepository.findByCustomerId(customerId,
                        LocalDateConverter.convertStartDate(startDate),
                        LocalDateConverter.convertEndDate(endDate)).stream()
                .map(OrderMapper.INSTANCE::orderToOrderResponse)
                .toList();

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Orders not found for customer id: " + customerId);
        }

        return orders;
    };

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) throws CustomException, ResourceNotFoundException {
        Order order = OrderMapper.INSTANCE.orderRequestToOrder(orderRequest);
        // Fetch customer's TRY asset
        Asset tryAsset = assetRepository.findByCustomerIdAndAssetId(order.getCustomerId(), "AST005")
                .orElseThrow(() -> new ResourceNotFoundException("Customer TRY asset not found"));

        // Calculate total cost for BUY
        double totalCost = order.getSize() * order.getPrice();

        if (order.getSide() == OrderSide.BUY) {
            if (tryAsset.getUsableSize() < totalCost) {
                throw new CustomException("Insufficient TRY balance");
            }
            // Deduct TRY usable size
            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalCost);
            AssetHelper.updateAsset(tryAsset);
            assetRepository.save(tryAsset);
        }

        Asset sellAsset = null;
        if (order.getSide() == OrderSide.SELL) {
            // Fetch the asset to buy/sell
            sellAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                    .orElseThrow(() -> new ResourceNotFoundException("Asset not found for customer"));

            if (sellAsset.getUsableSize() < order.getSize()) {
                throw new CustomException("Insufficient asset balance");
            }
            // Deduct asset usable size
            sellAsset.setUsableSize(sellAsset.getUsableSize() - order.getSize());
            AssetHelper.updateAsset(sellAsset);
            assetRepository.save(sellAsset);
            order.setAsset(sellAsset);
        }

        OrderHelper.setOrderDefaultValues(order);
        Order orderDB = orderRepository.save(order);
        return OrderMapper.INSTANCE.orderToOrderResponse(orderDB);
    }

    @Transactional
    public void cancelById(Long id) throws ResourceNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for id: " + id));

        if(!OrderStatus.PENDING.equals(order.getOrderStatus())) {
            throw new CustomException("Order is not in PENDING state");
        }

        if(order.getSide().equals(OrderSide.BUY)) {
            Asset asset = assetRepository.findByCustomerIdAndAssetId(order.getCustomerId(), "AST005")
                    .orElseThrow(() -> new ResourceNotFoundException("TRY asset not found for customer. First create try account for refund."));
            double totalCost = order.getSize() * order.getPrice();
            asset.setUsableSize(asset.getUsableSize() + totalCost);
            AssetHelper.updateAsset(asset);
            assetRepository.save(asset);
        }

        if(order.getSide().equals(OrderSide.SELL)) {
            Asset sellAsset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                    .orElseThrow(() -> new ResourceNotFoundException(order.getAssetName() + "not found for customer"));
            sellAsset.setUsableSize(sellAsset.getUsableSize() + order.getSize());
            AssetHelper.updateAsset(sellAsset);
            assetRepository.save(sellAsset);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        OrderHelper.updateOrder(order);
        orderRepository.save(order);
    }

    // TODO Carry this to utility class?





}
