package com.pm.productservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductRequest {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Min(value = 1, message = "Quantity can't be negative or zero")
    private int quantity;

    @NotNull(message = "Price is required")
    private Double price;

    @NotNull(message = "Product sale status is required")
    private Boolean isForSale;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;
}
