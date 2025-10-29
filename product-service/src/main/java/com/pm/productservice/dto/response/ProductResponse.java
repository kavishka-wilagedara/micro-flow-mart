package com.pm.productservice.dto.response;

import com.pm.productservice.model.ProductStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String category;
    private String name;
    private String description;
    private ProductStatus status;
    private int quantity;
    private Double price;
    private boolean isForSale;
    private LocalDate expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
