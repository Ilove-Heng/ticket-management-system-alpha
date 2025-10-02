package com.nangseakheng.order.mapper;

import com.nangseakheng.order.dto.OrderRequest;
import com.nangseakheng.order.dto.OrderResponse;
import com.nangseakheng.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Order toEntity(OrderRequest request);

    OrderResponse toResponse(Order order);
}