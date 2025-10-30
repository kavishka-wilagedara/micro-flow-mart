package com.pm.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDetailsRequest {

    @NotBlank(message = "Product ID required")
    private long productId;
    @NotBlank(message = "Quantity required")
    private int quantity;

}
