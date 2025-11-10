package com.pm.inventoryservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {

    private long orderId;
    private long productId;
    private String productName;
}
