package com.pm.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderResponse {

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
