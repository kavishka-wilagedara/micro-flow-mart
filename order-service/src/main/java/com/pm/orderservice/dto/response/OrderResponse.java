package com.pm.orderservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private ProductResponse productResponse;
    private int quantity;
    private double totalPrice;
    private LocalDateTime orderDate;

}
