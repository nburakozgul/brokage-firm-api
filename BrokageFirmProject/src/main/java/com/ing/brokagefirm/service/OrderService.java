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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetRepository assetRepository;

    private static final Logger LOGGER = LogManager.getLogger(OrderService.class);

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
            LOGGER.debug("Orders not found for customer id: " + customerId);
            throw new ResourceNotFoundException("Orders not found for customer id: " + customerId);
        }

        return orders;
    };

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) throws CustomException, ResourceNotFoundException {
        Order order = OrderMapper.INSTANCE.orderRequestToOrder(orderRequest);

        if (order.getSide() == OrderSide.BUY) {
            // Fetch customer's TRY asset
            Asset tryAsset = assetRepository.findByCustomerIdAndAssetId(order.getCustomerId(), "AST005")
                    .orElseThrow(() -> new ResourceNotFoundException("Customer TRY asset not found"));

            // Calculate total cost for BUY
            double totalCost = order.getSize() * order.getPrice();

            if (tryAsset.getUsableSize() < totalCost) {
                LOGGER.debug("Insufficient TRY balance");
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
                LOGGER.debug("Insufficient asset balance");
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
            LOGGER.debug("Order is not in PENDING state");
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
                    .orElseThrow(() -> new ResourceNotFoundException(order.getAssetName() + " not found for customer"));
            sellAsset.setUsableSize(sellAsset.getUsableSize() + order.getSize());
            AssetHelper.updateAsset(sellAsset);
            assetRepository.save(sellAsset);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        OrderHelper.updateOrder(order);
        orderRepository.save(order);
    }

    /*
    * if one buy or sell fails(db updates), all transactions rollback. it should be transaction per buy, not whole code
    * Optional was good for other method, but it causes a mess here
    * Good thing, it works and covers all cases
    * Needs proper unit testing
    * Divide method to parts, it will be more readable and easy to debug
    * (Implementation was last minute call)
    * */

    @Transactional
    public void matchPendingOrders() {
        List<Order> pendingOrder = orderRepository.findOrdersByOrderStatus(OrderStatus.PENDING);
        // 1. Fetch all pending BUY orders
        List<Order> buyOrders = pendingOrder.stream().filter(o -> OrderSide.BUY.equals(o.getSide())).sorted(Comparator.comparing(Order::getCreatedAt)).toList();

        // 2. Fetch all pending SELL orders
        List<Order> sellOrders = pendingOrder.stream().filter(o -> OrderSide.SELL.equals(o.getSide())).toList();

        buyOrders.forEach(buyOrder -> {
            double buyRemaining = buyOrder.getSize();
            List<Order> sellOrdersWithCustomer = sellOrders.stream()
                    .filter(sellOrder ->
                        sellOrder.getAssetName().equals(buyOrder.getAssetName()) && sellOrder.getPrice() <= buyOrder.getPrice()
                    )
                    .sorted(Comparator.comparing(Order::getPrice))
                    .toList();

            Iterator<Order> sellIterator = sellOrdersWithCustomer.iterator();
            while (buyRemaining > 0 && sellIterator.hasNext()) {
                Order sellOrder = sellIterator.next();
                double sellRemaining = sellOrder.getSize();

                // Matched quantity
                double matchedSize = Math.min(buyRemaining, sellRemaining);

                // Get Asset from DB
                Optional<Asset> tryAssetBuyOpt = assetRepository.findByCustomerIdAndAssetId(buyOrder.getCustomerId(), "AST005");
                Optional<Asset> tryAssetSellOpt = assetRepository.findByCustomerIdAndAssetId(sellOrder.getCustomerId(), "AST005");
                Optional<Asset> targetAssetBuyOpt = assetRepository.findByCustomerIdAndAssetName(buyOrder.getCustomerId(), buyOrder.getAssetName());
                Optional<Asset> targetAssetSellOpt = assetRepository.findByCustomerIdAndAssetName(sellOrder.getCustomerId(), sellOrder.getAssetName());

                if (tryAssetSellOpt.isEmpty() || targetAssetSellOpt.isEmpty())
                    continue;

                if (tryAssetBuyOpt.isEmpty())
                    break;

                Asset targetAssetBuy = null;

                // Create asset we want to buy if we don't have yet
                if (targetAssetBuyOpt.isEmpty()) {
                    targetAssetBuy = AssetHelper.createAssetWithCustomerId(tryAssetBuyOpt.get().getCustomerId(), targetAssetSellOpt.get());
                } else {
                    targetAssetBuy = targetAssetBuyOpt.get();
                }

                Asset tryAssetSell = tryAssetSellOpt.get();
                Asset tryAssetBuy = tryAssetBuyOpt.get();
                Asset targetAssetSell = targetAssetSellOpt.get();

                double totalCost = matchedSize * sellOrder.getPrice();

                // Buyer: deduct TRY (already locked in usableSize), add asset
                tryAssetBuy.setSize(tryAssetBuy.getSize() - totalCost);
                targetAssetBuy.setSize(targetAssetBuy.getSize() + matchedSize);
                targetAssetBuy.setUsableSize(targetAssetBuy.getUsableSize() + matchedSize);

                // Seller: add TRY, deduct asset (already locked in usableSize)
                tryAssetSell.setSize(tryAssetSell.getSize() + totalCost);
                targetAssetSell.setSize(targetAssetSell.getSize() - matchedSize);
                targetAssetSell.setUsableSize(targetAssetSell.getUsableSize() - matchedSize);

                assetRepository.saveAll(Arrays.asList(tryAssetBuy, tryAssetSell, targetAssetBuy, targetAssetSell));

                // Update orders
                buyRemaining -= matchedSize;
                sellRemaining -= matchedSize;

                sellOrder.setSize(sellRemaining);
                if (sellRemaining == 0) {
                    sellOrder.setOrderStatus(OrderStatus.MATCHED);
                }
                orderRepository.save(sellOrder);

                if (buyRemaining == 0) {
                    buyOrder.setSize(0.0);
                    buyOrder.setOrderStatus(OrderStatus.MATCHED);
                    orderRepository.save(buyOrder);
                    break; // next BUY order
                }
            }

            // Update remaining size if partially filled
            if (buyRemaining > 0) {
                buyOrder.setSize(buyRemaining);
                orderRepository.save(buyOrder);
            }
        });
    }

}
