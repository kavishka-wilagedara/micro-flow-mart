package com.pm.orderservice.service.impl;

import com.pm.orderservice.dto.request.OrderRequest;
import com.pm.orderservice.dto.response.OrderResponse;
import com.pm.orderservice.dto.response.ProductOrderResponse;
import com.pm.orderservice.dto.response.ProductResponse;
import com.pm.orderservice.repo.OrderRepository;
import com.pm.orderservice.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        long productId = orderRequest.getProductId();

        try{
            ProductResponse productResponseFromService = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("http://localhost:8081/api/v1/products/get/{productId}").build(productId))
                    .retrieve()
                    .bodyToMono(ProductResponse.class)
                    .block();

        }
        catch (){}

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
