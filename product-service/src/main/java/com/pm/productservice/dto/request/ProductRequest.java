package com.pm.productservice.dto.request;

import com.pm.productservice.model.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductRequest {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Status is required")
    private ProductStatus status;

    @NotBlank(message = "Quantity is required")
    private String quantity;

    @NotBlank(message = "Price is required")
    private Double price;

    @NotBlank(message = "Product sale status is required")
    private Boolean isForSale;

    @NotBlank(message = "Expiry date is required")
    private LocalDate expiryDate;
}
