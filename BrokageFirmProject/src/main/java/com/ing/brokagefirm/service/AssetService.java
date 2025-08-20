package com.ing.brokagefirm.service;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.exception.ResourceAlreadyExists;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.mapper.AssetMapper;
import com.ing.brokagefirm.model.AssetRequest;
import com.ing.brokagefirm.model.AssetResponse;
import com.ing.brokagefirm.repository.AssetRepository;
import com.ing.brokagefirm.utility.AssetHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    public AssetResponse findById(Long id) throws ResourceNotFoundException {
        return assetRepository.findById(id).map(AssetMapper.INSTANCE::assetToAssetResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found for id: " + id));
    };

    public List<AssetResponse> findByCustomerId(String customerId) throws ResourceNotFoundException {
        List<AssetResponse> assets = assetRepository.findByCustomerId(customerId).
                stream().map(AssetMapper.INSTANCE::assetToAssetResponse)
                .toList();

        if (assets.isEmpty()) {
            throw new ResourceNotFoundException("Assets not found for customer id: " + customerId);
        }

        return assets;
    };

    public AssetResponse createAsset(AssetRequest asset) throws ResourceAlreadyExists {
        Optional<Asset> assetDB = assetRepository.findByCustomerIdAndAssetName(asset.getCustomerId(), asset.getAssetName());
        if(assetDB.isPresent()){
            throw new ResourceAlreadyExists("Asset already exist for customer");
        }
        Asset assetSave = AssetMapper.INSTANCE.assetRequestToAsset(asset);
        AssetHelper.setAssetDefaultValues(assetSave);
        Asset assetSaved = assetRepository.save(assetSave);
        return AssetMapper.INSTANCE.assetToAssetResponse(assetSaved);
    }

    public void deleteById(Long id){
        try {
            assetRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Asset not found for id: " + id);
        }

    }
}
