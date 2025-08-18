package com.ing.brokagefirm.service;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.mapper.AssetMapper;
import com.ing.brokagefirm.model.AssetRequest;
import com.ing.brokagefirm.model.AssetResponse;
import com.ing.brokagefirm.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    public AssetResponse findById(Long id){
        return assetRepository.findById(id).map(AssetMapper.INSTANCE::assetToAssetResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found for id: " + id));
    };

    public List<AssetResponse> findByCustomerId(String customerId){
        return assetRepository.findByCustomerId(customerId).
                stream().map(AssetMapper.INSTANCE::assetToAssetResponse)
                .toList();
    };

    public List<Asset> findAll(){
        return assetRepository.findAll();
    }

    public AssetResponse createAsset(AssetRequest asset){
         Asset assetDB = assetRepository.save(AssetMapper.INSTANCE.assetRequestToAsset(asset));
         return AssetMapper.INSTANCE.assetToAssetResponse(assetDB);
    }

    public void deleteById(Long id){
        assetRepository.deleteById(id);
    }
}
