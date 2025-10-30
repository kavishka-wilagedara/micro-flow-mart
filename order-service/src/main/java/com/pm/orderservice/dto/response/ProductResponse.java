package com.pm.orderservice.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

        private Long id;
        private String category;
        private String name;
        private String description;
        private String status;
        private int quantity;
        private Double price;
        private boolean isForSale;
        private LocalDate expiryDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

}
