package com.ing.brokagefirm.mapper;

import com.ing.brokagefirm.entity.Asset;
import com.ing.brokagefirm.model.AssetRequest;
import com.ing.brokagefirm.model.AssetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    AssetResponse assetToAssetResponse(Asset order);

    Asset assetRequestToAsset(AssetRequest assetRequest);
}
