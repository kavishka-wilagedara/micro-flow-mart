package com.pm.inventoryservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryRequest {

    @NotNull(message = "Order ID required")
    private long orderId;
    @NotNull(message = "Product ID required")
    private long productId;
    @NotNull(message = "Product name required")
    private String productName;

}
