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
public class OrderDeletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderDeletedConsumer.class);

    private final InventoryService inventoryService;

    public OrderDeletedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${kafka.topic.order.deleted}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrderDeleteEvent(OrderEvent orderEvent) {
        try {
            log.info("Order deleted event: {}", orderEvent);

            long orderId = orderEvent.getOrderId();
            inventoryService.markAsDeletedOrder(orderId);
        }catch (Exception ex){
            log.error("Failed receiving order event from order-service {}", ex.getMessage());
            throw new ConsumerFailedException("Order event receiving failed");
        }
    }
}
