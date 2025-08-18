package com.ing.brokagefirm.mapper;

import com.ing.brokagefirm.entity.Order;
import com.ing.brokagefirm.model.OrderRequest;
import com.ing.brokagefirm.model.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponse orderToOrderResponse(Order order);

    Order orderRequestToOrder(OrderRequest orderRequest);
}