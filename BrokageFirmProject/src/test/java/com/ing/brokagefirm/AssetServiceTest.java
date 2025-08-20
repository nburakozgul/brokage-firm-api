package com.ing.brokagefirm;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.exception.ResourceAlreadyExists;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.model.AssetRequest;
import com.ing.brokagefirm.model.AssetResponse;
import com.ing.brokagefirm.repository.AssetRepository;
import com.ing.brokagefirm.service.AssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceTest {

    @InjectMocks
    private AssetService assetService;

    @Mock
    private AssetRepository assetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- findById ----------

    @Test
    void testFindById_Found() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setCustomerId("CUST001");
        asset.setAssetName("Apple Inc.");
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        AssetResponse response = assetService.findById(1L);

        assertNotNull(response);
        assertEquals("CUST001", response.getCustomerId());
        assertEquals("Apple Inc.", response.getAssetName());
        verify(assetRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> assetService.findById(99L));
    }

    // ---------- findByCustomerId ----------

    @Test
    void testFindByCustomerId_Found() {
        Asset asset1 = new Asset();
        asset1.setId(1L);
        asset1.setCustomerId("CUST001");
        Asset asset2 = new Asset();
        asset2.setId(2L);
        asset2.setCustomerId("CUST001");
        asset2.setAssetName("Apple Inc.");

        when(assetRepository.findByCustomerId("CUST001")).thenReturn(List.of(asset1, asset2));

        List<AssetResponse> responses = assetService.findByCustomerId("CUST001");
        assertEquals(2, responses.size());
        assertFalse(responses.isEmpty());
        assertEquals("Apple Inc.", responses.get(1).getAssetName());
        verify(assetRepository).findByCustomerId("CUST001");
    }

    @Test
    void testFindByCustomerId_NotFound() {
        when(assetRepository.findByCustomerId("CUST001")).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> assetService.findByCustomerId("CUST001"));
    }

    // ---------- createAsset ----------

    @Test
    void testCreateAsset_Success() {
        AssetRequest request = new AssetRequest();
        request.setCustomerId("CUST003");
        request.setAssetName("Amazon.com Inc.");

        Asset asset = new Asset();
        asset.setId(3L);
        asset.setCustomerId("CUST003");
        asset.setAssetName("Amazon.com Inc.");

        when(assetRepository.findByCustomerIdAndAssetName("CUST003", "Amazon.com Inc."))
                .thenReturn(Optional.empty());
        when(assetRepository.save(any(Asset.class)))
                .thenAnswer(invocation -> {
                    Asset a = invocation.getArgument(0);
                    a.setId(3L);
                    return a;
                });

        AssetResponse response = assetService.createAsset(request);

        assertNotNull(response);
        assertEquals("CUST003", response.getCustomerId());
        assertEquals("Amazon.com Inc.", response.getAssetName());
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void testCreateAsset_AlreadyExists() {
        AssetRequest request = new AssetRequest();
        request.setCustomerId("CUST004");
        request.setAssetName("Bitcoin");

        Asset existingAsset = new Asset();
        existingAsset.setCustomerId("CUST004");
        existingAsset.setAssetName("Bitcoin");

        when(assetRepository.findByCustomerIdAndAssetName("CUST004", "Bitcoin"))
                .thenReturn(Optional.of(existingAsset));

        assertThrows(ResourceAlreadyExists.class, () -> assetService.createAsset(request));
        verify(assetRepository, never()).save(any());
    }

    // ---------- deleteById ----------

    @Test
    void testDeleteById_Success() {
        assetRepository.deleteById(10L);
        verify(assetRepository).deleteById(10L);
        verify(assetRepository, times(1)).deleteById(10L);
    }

    @Test
    void testDeleteById_NotFound() {
        doThrow(new RuntimeException("Not found")).when(assetRepository).deleteById(20L);
        assertThrows(ResourceNotFoundException.class, () -> assetService.deleteById(20L));
        verify(assetRepository).deleteById(20L);
    }
}
