package com.pm.orderservice.dto.response;

import com.pm.orderservice.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private long orderId;
    private ProductOrderResponse productOrderResponse;
    private int quantity;
    private double totalPrice;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private RecieverDetailsResponse recieverDetailsResponse;
    private RecieverAddressResponse recieverAddressResponse;
}
