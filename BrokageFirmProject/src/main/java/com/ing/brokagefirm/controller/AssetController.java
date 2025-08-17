package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/asset")
public class AssetController {
    @Autowired
    private AssetService assetService;

    //TODO use custom request response models(not entity classes)

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable(value = "assetId") Long assetId) throws ResourceNotFoundException {
        Asset asset = assetService
                        .findById(assetId)
                        .orElseThrow(() -> new ResourceNotFoundException("Asset not found for id: " + assetId));
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping
    public ResponseEntity<List<Asset>> getAssetByCustomerId(@RequestParam(value = "customerId") String customerId) throws ResourceNotFoundException {
        List<Asset> asset = assetService
                .findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found for customer id: " + customerId));
        return ResponseEntity.ok().body(asset);
    }

    @PostMapping("")
    public ResponseEntity createAsset(@RequestBody Asset asset) throws CustomException {
        Asset assetDB;
        try{
            assetDB = assetService.saveAsset(asset);
        }catch (Exception e){
            throw new CustomException("Create process failed error message : " + e.getMessage());
        }

        return ResponseEntity.ok().body(assetDB);
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity deleteReviewById(@PathVariable(value = "assetId") Long assetId) throws ResourceNotFoundException {
        try{
            assetService.deleteById(assetId);
        }catch (Exception e){
            throw new ResourceNotFoundException("Asset not found. Error message: " + e.getMessage());
        }

        return ResponseEntity.ok().body("Asset deleted");
    }
}
