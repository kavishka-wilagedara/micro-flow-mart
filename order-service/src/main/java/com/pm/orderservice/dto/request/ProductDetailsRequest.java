package com.pm.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDetailsRequest {

    @NotNull(message = "Product ID required")
    private Long productId;
    @NotNull(message = "Quantity required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
