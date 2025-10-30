package com.pm.orderservice.service.impl;

import com.pm.orderservice.dto.request.OrderRequest;
import com.pm.orderservice.dto.response.OrderResponse;
import com.pm.orderservice.repo.OrderRepository;
import com.pm.orderservice.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderResponse updateOrder(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public void deleteOrder(long orderId) {

    }

    @Override
    public OrderResponse getOrder(long orderId) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return List.of();
    }
}
