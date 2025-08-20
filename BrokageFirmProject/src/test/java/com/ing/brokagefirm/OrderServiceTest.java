package com.ing.brokagefirm;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.model.*;
import com.ing.brokagefirm.repository.AssetRepository;
import com.ing.brokagefirm.repository.OrderRepository;
import com.ing.brokagefirm.service.OrderService;
import com.ing.brokagefirm.utility.LocalDateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    // ---------- findById ----------

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

    // ---------- findByCustomerId ----------

    @Test
    void testFindByCustomerId_WithDates() {
        String customerId = "CUST001";
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        Order order = new Order();
        order.setId(1L);
        order.setCustomerId(customerId);
        order.setSize(100.0);
        order.setCreatedAt(LocalDateTime.of(2025, 1, 15, 10, 0));

        when(orderRepository.findByCustomerId(
                eq(customerId),
                eq(LocalDateConverter.convertStartDate(start)),
                eq(LocalDateConverter.convertEndDate(end))
        )).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.findByCustomerId(customerId, start, end);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(customerId, responses.get(0).getCustomerId());

        verify(orderRepository).findByCustomerId(
                eq(customerId),
                eq(LocalDateConverter.convertStartDate(start)),
                eq(LocalDateConverter.convertEndDate(end))
        );
    }

    // ---------- createAsset ----------

    @Test
    void testCreateOrder_Buy_Success() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("CUST001");
        request.setSide(OrderSide.BUY);
        request.setSize(100.0);
        request.setPrice(2.0);

        Asset tryAsset = new Asset();
        tryAsset.setUsableSize(1000.0);

        when(assetRepository.findByCustomerIdAndAssetId("CUST001", "AST005"))
                .thenReturn(Optional.of(tryAsset));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals("CUST001", response.getCustomerId());
        assertEquals(100.0, response.getSize());
        verify(assetRepository).save(tryAsset);
        verify(orderRepository).save(any(Order.class));
        assertEquals(800.0, tryAsset.getUsableSize());
    }

    @Test
    void testCreateOrder_Buy_InsufficientTRYBalance() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("CUST004");
        request.setSide(OrderSide.BUY);
        request.setSize(1000.0);
        request.setPrice(1.0);

        Asset tryAsset = new Asset();
        tryAsset.setUsableSize(500.0);

        when(assetRepository.findByCustomerIdAndAssetId("CUST004", "AST005")).thenReturn(Optional.of(tryAsset));

        assertThrows(CustomException.class, () -> orderService.createOrder(request));
        verify(assetRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_Buy_TryAssetNotFound() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("CUST003");
        request.setSide(OrderSide.BUY);
        request.setSize(50.0);
        request.setPrice(10.0);

        when(assetRepository.findByCustomerIdAndAssetId("CUST003", "AST005"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_Sell_Success() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("CUST004");
        request.setSide(OrderSide.SELL);
        request.setSize(100.0);
        request.setPrice(5.0);
        request.setAssetName("Apple Inc.");

        Asset sellAsset = new Asset();
        sellAsset.setUsableSize(200.0);
        sellAsset.setAssetName("Apple Inc.");
        when(assetRepository.findByCustomerIdAndAssetName("CUST004", "Apple Inc."))
                .thenReturn(Optional.of(sellAsset));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        verify(assetRepository).save(sellAsset);
        verify(orderRepository).save(any(Order.class));
        assertEquals(100.0, response.getSize());
        assertEquals(100.0, sellAsset.getUsableSize()); // 200 - 100
    }

    @Test
    void testCreateOrder_Sell_InsufficientAssetBalance() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("CUST004");
        request.setSide(OrderSide.SELL);
        request.setSize(300.0);
        request.setPrice(5.0);
        request.setAssetName("Apple Inc.");

        Asset sellAsset = new Asset();
        sellAsset.setUsableSize(100.0);
        when(assetRepository.findByCustomerIdAndAssetName("CUST004", "Apple Inc."))
                .thenReturn(Optional.of(sellAsset));

        assertThrows(CustomException.class, () -> orderService.createOrder(request));
        verify(assetRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrder_Sell_AssetNotFound() {
        OrderRequest request = new OrderRequest();
        request.setCustomerId("CUST004");
        request.setSide(OrderSide.SELL);
        request.setSize(100.0);
        request.setPrice(5.0);
        request.setAssetName("Apple Inc.");

        when(assetRepository.findByCustomerIdAndAssetName("CUST004", "Apple Inc."))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    // Cancel Order Test

    @Test
    void testCancelById_Buy_Success() {
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
        orderService.cancelById(1L);

        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        assertEquals(70.0, tryAsset.getUsableSize());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCancelById_Sell_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPrice(20.0);
        order.setSize(2.0);
        order.setAssetName("Apple Inc.");
        order.setSide(OrderSide.SELL);

        Asset sellAsset = new Asset();
        sellAsset.setUsableSize(50.0);
        sellAsset.setAssetName("Apple Inc.");
        sellAsset.setAssetId("AST001");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), "Apple Inc.")).thenReturn(Optional.of(sellAsset));
        orderService.cancelById(1L);

        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        assertEquals(52.0, sellAsset.getUsableSize());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCancelById_Sell_ResourceNotFound() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPrice(20.0);
        order.setSize(2.0);
        order.setAssetName("Bitcoin");
        order.setSide(OrderSide.SELL);

        Asset sellAsset = new Asset();
        sellAsset.setUsableSize(50.0);
        sellAsset.setAssetName("Apple Inc.");
        sellAsset.setAssetId("AST001");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertThrows(ResourceNotFoundException.class, () -> orderService.cancelById(1L));
        verify(assetRepository).findByCustomerIdAndAssetName(order.getCustomerId(), "Bitcoin");
    }
}
