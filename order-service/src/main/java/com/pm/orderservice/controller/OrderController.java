package com.pm.orderservice.controller;

import com.pm.orderservice.dto.request.OrderRequest;
import com.pm.orderservice.dto.response.OrderResponse;
import com.pm.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody OrderRequest orderRequest){

        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        return ResponseEntity.ok().body(orderResponse);
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long orderId){
        OrderResponse orderResponse = orderService.getOrder(orderId);

        return ResponseEntity.ok().body(orderResponse);
    }
}
