package com.pm.orderservice.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent implements Serializable {

    private long orderId;
    private long productId;
    private String productName;

    public OrderEvent(long orderId) {
        this.orderId = orderId;
    }
}
