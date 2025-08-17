package com.ing.brokagefirm.mapper;

import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.model.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderRequest orderToOrderRequest(Order order);

    @Mapping(source = "orderRequest.side", target = "orderSide")
    Order orderRequestToOrder(OrderRequest orderRequest);
}