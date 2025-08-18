package com.ing.brokagefirm.controller;

import com.ing.brokagefirm.exception.CustomException;
import com.ing.brokagefirm.exception.ResourceNotFoundException;
import com.ing.brokagefirm.model.AssetRequest;
import com.ing.brokagefirm.model.AssetResponse;
import com.ing.brokagefirm.service.AssetService;
import jakarta.validation.Valid;
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
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable(value = "assetId") Long assetId) throws ResourceNotFoundException {
        AssetResponse assetResponse = assetService.findById(assetId);
        return ResponseEntity.ok().body(assetResponse);
    }

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAssetByCustomerId(@RequestParam(value = "customerId") String customerId) throws ResourceNotFoundException {
        List<AssetResponse> assets = assetService
                .findByCustomerId(customerId);

        if (assets.isEmpty()) {
            throw new ResourceNotFoundException("Assets not found for customer id: " + customerId);
        }

        return ResponseEntity.ok().body(assets);
    }

    @PostMapping
    public ResponseEntity createAsset(@RequestBody @Valid AssetRequest assetRequest) throws CustomException {
        AssetResponse assetResponse;
        try {
            assetResponse = assetService.createAsset(assetRequest);
        } catch (Exception e) {
            throw new CustomException("Create process failed error message : " + e.getMessage());
        }

        return ResponseEntity.ok().body(assetResponse);
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity deleteAssetById(@PathVariable(value = "assetId") Long assetId) throws ResourceNotFoundException {
        try {
            assetService.deleteById(assetId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Asset not found. Error message: " + e.getMessage());
        }

        return ResponseEntity.ok().body("Asset deleted");
    }
}
