package com.pm.orderservice.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductOrderResponse{

    private String category;
    private String name;
    private String description;
    private String status;
    private Double price;
    private boolean isForSale;
    private LocalDate expiryDate;
}
