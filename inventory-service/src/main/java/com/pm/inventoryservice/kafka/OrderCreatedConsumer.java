package com.pm.inventoryservice.kafka;

import com.pm.inventoryservice.dto.event.OrderEvent;
import com.pm.inventoryservice.dto.request.InventoryRequest;
import com.pm.inventoryservice.model.Inventory;
import com.pm.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final InventoryService inventoryService;

    public OrderCreatedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${kafka.topic.order.created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrderCreateEvent(OrderEvent orderEvent) {
        try {
            InventoryRequest inventoryRequest = InventoryRequest.builder()
                    .orderId(orderEvent.getOrderId())
                    .productId(orderEvent.getProductId())
                    .productName(orderEvent.getProductName())
                    .build();

            log.info("Receive order event from order-service {}", orderEvent);
            inventoryService.createInventory(inventoryRequest);
        }catch (Exception e) {
            log.error("Failed receiving order event from order-service {}", e.getMessage());
        }
    }

}
