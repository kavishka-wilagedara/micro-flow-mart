package com.pm.orderservice.kafka;

import com.pm.orderservice.dto.events.OrderEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final NewTopic orderCreatedTopic;

    public OrderProducer(
            NewTopic orderCreatedTopic,
            KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.orderCreatedTopic = orderCreatedTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderEvent orderEvent) {
        sendMessage(orderCreatedTopic.name(), orderEvent);
    }

    public void sendMessage(String topic, OrderEvent orderEvent) {
        log.info("Sending event: {}", orderEvent);

        Message<OrderEvent> message = MessageBuilder
                .withPayload(orderEvent)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        try {
            kafkaTemplate.send(message);
            log.info("Event sent successfully to topic {}: {}", topic, orderEvent);
        }catch (Exception ex){
            log.error("Error while sending event to topic {}: {}", topic, ex.getMessage());
        }
    }
}
