package com.pm.inventoryservice.model;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private long OrderId;
    @NotNull
    private long ProductId;
    @NotNull
    private String ProductName;
    @NotNull
    private int quantity;
    @NotNull
    private double unitPrice;
    @NotNull
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
