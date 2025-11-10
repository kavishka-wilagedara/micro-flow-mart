package com.pm.orderservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.order.created}")
    private String orderCreatedTopic;

    @Value("${kafka.topic.order.updated}")
    private String orderUpdatedTopic;

    @Value("${kafka.topic.order.deleted}")
    private String orderDeletedTopic;

    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder
                .name(orderCreatedTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderUpdatedTopic() {
        return TopicBuilder
                .name(orderUpdatedTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderDeletedTopic() {
        return TopicBuilder
                .name(orderDeletedTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
