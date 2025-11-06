package com.pm.orderservice.service;

import com.pm.orderservice.dto.request.OrderRequest;
import com.pm.orderservice.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse updateOrder(OrderRequest orderRequest, long orderId);
    void deleteOrder(long orderId);
    OrderResponse getOrder(long orderId);
    List<OrderResponse> getAllOrders();
}
