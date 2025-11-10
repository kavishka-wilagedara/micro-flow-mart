package com.pm.inventoryservice.kafka;

import com.pm.inventoryservice.dto.event.OrderEvent;
import com.pm.inventoryservice.dto.request.InventoryRequest;
import com.pm.inventoryservice.exception.ConsumerFailedException;
import com.pm.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderUpdatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderUpdatedConsumer.class);

    private final InventoryService inventoryService;

    public OrderUpdatedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${kafka.topic.order.updated}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrderUpdateEvent(OrderEvent orderEvent) {

        try {
            log.info("Received order updated event: {}", orderEvent);

            InventoryRequest updateInventoryRequest = InventoryRequest.builder()
                    .orderId(orderEvent.getOrderId())
                    .productId(orderEvent.getProductId())
                    .productName(orderEvent.getProductName())
                    .build();

            inventoryService.updateInventory(updateInventoryRequest, orderEvent.getOrderId());
        }catch (Exception ex){
            log.error("Failed receiving order event from order-service {}", ex.getMessage());
            throw new ConsumerFailedException("Order event receiving failed");
        }
    }
}
