package com.pm.inventoryservice.dto.response;

import com.pm.inventoryservice.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryResponse {

    private long id;
    private long orderId;
    private long productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
