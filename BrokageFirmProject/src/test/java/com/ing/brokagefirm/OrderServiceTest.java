package com.ing.brokagefirm;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.model.*;
import com.ing.brokagefirm.repository.AssetRepository;
import com.ing.brokagefirm.repository.OrderRepository;
import com.ing.brokagefirm.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setAssetName("Apple Inc.");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.findById(1L);
        assertEquals("Apple Inc.", response.getAssetName());
    }

    @Test
    void testFindById_NotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.findById(99L));
    }

    @Test
    void testCreateOrder_Buy_InsufficientTRY() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("CUST004");
        request.setSide(OrderSide.BUY);
        request.setSize(1000.0);
        request.setPrice(1.0);

        Asset tryAsset = new Asset();
        tryAsset.setUsableSize(500.0);

        when(assetRepository.findByCustomerIdAndAssetId("CUST004", "AST005")).thenReturn(Optional.of(tryAsset));

        assertThrows(CustomException.class, () -> orderService.createOrder(request));
    }

    @Test
    void testCancelById_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPrice(20.0);
        order.setSize(1.0);
        order.setSide(OrderSide.BUY);

        Asset tryAsset = new Asset();
        tryAsset.setUsableSize(50.0);
        tryAsset.setAssetName("TRY");
        tryAsset.setAssetId("AST005");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(assetRepository.findByCustomerIdAndAssetId(order.getCustomerId(), "AST005")).thenReturn(Optional.of(tryAsset));
        assetRepository.findByCustomerIdAndAssetId(order.getCustomerId(), "AST005");
        orderService.cancelById(1L);

        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        assertEquals(70.0, tryAsset.getUsableSize());
        verify(orderRepository, times(1)).save(order);
    }
}
