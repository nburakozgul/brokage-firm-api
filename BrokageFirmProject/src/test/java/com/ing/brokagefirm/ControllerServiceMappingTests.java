package com.ing.brokagefirm;

import com.ing.brokagefirm.controller.AssetController;
import com.ing.brokagefirm.controller.OrderController;
import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.model.*;
import com.ing.brokagefirm.service.AssetService;
import com.ing.brokagefirm.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControllerServiceMappingTests {

    @Mock
    private AssetService assetService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private AssetController assetController;

    @InjectMocks
    private OrderController orderController;

    private AssetResponse assetResponse;
    private AssetRequest assetRequest;
    private OrderResponse orderResponse;
    private OrderRequest orderRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        assetResponse = new AssetResponse("CUST001", "AST001", "Apple Inc.", "AAPL stock", 100.0, 80.0);
        assetRequest = new AssetRequest("CUST001", "AST001", "Apple Inc.", "AAPL stock", 100.0, 80.0);

        orderResponse = new OrderResponse("CUST001", "Apple Inc.", OrderSide.BUY, OrderStatus.PENDING, 10, 175.5);
        orderRequest = new OrderRequest("CUST001", OrderSide.BUY, 175.5, 10.0, "AST001", "Apple Inc.");
    }

    // ==================== AssetController Tests ====================
    @Test
    void testGetAssetById_Success() {
        when(assetService.findById(1L)).thenReturn(assetResponse);

        ResponseEntity<AssetResponse> response = assetController.getAssetById(1L);

        assertNotNull(response);
        assertEquals("CUST001", response.getBody().getCustomerId());
        verify(assetService, times(1)).findById(1L);
    }

    @Test
    void testGetAssetByCustomerId_Success() throws ResourceNotFoundException {
        when(assetService.findByCustomerId("CUST001")).thenReturn(List.of(assetResponse));

        ResponseEntity<List<AssetResponse>> response = assetController.getAssetByCustomerId("CUST001");

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(assetService, times(1)).findByCustomerId("CUST001");
    }

    @Test
    void testCreateAsset_Success() throws CustomException {
        when(assetService.createAsset(assetRequest)).thenReturn(assetResponse);

        ResponseEntity response = assetController.createAsset(assetRequest);

        assertNotNull(response);
        assertEquals(assetResponse, response.getBody());
        verify(assetService, times(1)).createAsset(assetRequest);
    }

    @Test
    void testDeleteAssetById_Success() throws ResourceNotFoundException {
        doNothing().when(assetService).deleteById(1L);

        ResponseEntity response = assetController.deleteAssetById(1L);

        assertNotNull(response);
        assertEquals("Asset deleted", response.getBody());
        verify(assetService, times(1)).deleteById(1L);
    }

    // ==================== OrderController Tests ====================
    @Test
    void testGetOrderById_Success() {
        when(orderService.findById(1L)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.getOrderById(1L);

        assertNotNull(response);
        assertEquals("CUST001", response.getBody().getCustomerId());
        verify(orderService, times(1)).findById(1L);
    }

    @Test
    void testGetOrderByCustomerId_Success() throws ResourceNotFoundException {
        when(orderService.findByCustomerId("CUST001")).thenReturn(List.of(orderResponse));

        ResponseEntity<List<OrderResponse>> response = orderController.getOrderByCustomerId("CUST001");

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(orderService, times(1)).findByCustomerId("CUST001");
    }

    @Test
    void testCreateOrder_Success() throws CustomException {
        when(orderService.createOrder(orderRequest)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> response = orderController.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(orderResponse, response.getBody());
        verify(orderService, times(1)).createOrder(orderRequest);
    }

    @Test
    void testCancelOrder_Success() throws ResourceNotFoundException {
        doNothing().when(orderService).cancelById(1L);

        ResponseEntity response = orderController.cancelOrder(1L);

        assertNotNull(response);
        assertEquals("Order cancelled", response.getBody());
        verify(orderService, times(1)).cancelById(1L);
    }
}
