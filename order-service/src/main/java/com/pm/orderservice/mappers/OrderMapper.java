package com.pm.orderservice.mappers;

import com.pm.orderservice.dto.response.OrderResponse;
import com.pm.orderservice.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toOrderResponse(Order order);
}
