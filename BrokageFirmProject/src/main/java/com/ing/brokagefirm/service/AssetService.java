package com.ing.brokagefirm.service;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    public Optional<Asset> findById(Long id){
        return assetRepository.findById(id);
    };

    public Optional<List<Asset>> findByCustomerId(String customerId){
        return assetRepository.findByCustomerId(customerId);
    };

    public List<Asset> findAll(){
        return assetRepository.findAll();
    }

    // TODO
    public Asset createAsset(Asset asset){
        return assetRepository.save(asset);
    }

    public void deleteById(Long id){
        assetRepository.deleteById(id);
    }
}
