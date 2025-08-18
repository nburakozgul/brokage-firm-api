package com.ing.brokagefirm.repository;

import com.ing.brokagefirm.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset,Long> {
    List<Asset> findByCustomerId(String customerId);
    Optional<Asset> findByCustomerIdAndAssetId(String customerId, String assetId);
    Optional<Asset> findByCustomerIdAndAssetName(String customerId, String assetName);
}
