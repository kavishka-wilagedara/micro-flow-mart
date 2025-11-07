package com.pm.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {

    @NotNull(message = "Order ID required")
    private long orderId;
    @NotNull(message = "Product ID required")
    private long productId;
    @NotNull(message = "Product name required")
    private String productName;
    @NotNull(message = "Order quantity required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    @NotNull(message = "Unit price required")
    private double unitPrice;
    @NotNull(message = "Total price required")
    private double totalPrice;
}
