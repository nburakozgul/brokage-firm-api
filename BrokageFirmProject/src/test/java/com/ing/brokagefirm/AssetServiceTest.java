package com.ing.brokagefirm;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.model.AssetRequest;
import com.ing.brokagefirm.model.AssetResponse;
import com.ing.brokagefirm.repository.AssetRepository;
import com.ing.brokagefirm.service.AssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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

    @Test
    void testFindById_Success() {
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setAssetName("Apple Inc.");
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));

        AssetResponse response = assetService.findById(1L);

        assertEquals("Apple Inc.", response.getAssetName());
        verify(assetRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> assetService.findById(99L));
    }

    @Test
    void testFindByCustomerId() {
        Asset asset1 = new Asset();
        asset1.setId(1L);
        asset1.setCustomerId("CUST001");
        Asset asset2 = new Asset();
        asset2.setId(2L);
        asset2.setCustomerId("CUST001");

        when(assetRepository.findByCustomerId("CUST001")).thenReturn(List.of(asset1, asset2));

        List<AssetResponse> responses = assetService.findByCustomerId("CUST001");
        assertEquals(2, responses.size());
    }

    @Test
    void testCreateAsset() {
        AssetRequest request = new AssetRequest();
        request.setAssetName("Amazon");

        Asset savedAsset = new Asset();
        savedAsset.setId(1L);
        savedAsset.setAssetName("Amazon");

        when(assetRepository.save(any(Asset.class))).thenReturn(savedAsset);

        AssetResponse response = assetService.createAsset(request);
        assertEquals("Amazon", response.getAssetName());
    }

    @Test
    void testDeleteById() {
        assetService.deleteById(1L);
        verify(assetRepository, times(1)).deleteById(1L);
    }
}
