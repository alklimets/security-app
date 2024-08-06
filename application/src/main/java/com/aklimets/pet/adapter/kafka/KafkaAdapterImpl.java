package com.aklimets.pet.adapter.kafka;

import com.aklimets.pet.buildingblock.interfaces.DomainEvent;
import com.aklimets.pet.domain.event.DomainEventAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaAdapterImpl implements DomainEventAdapter {

    @Autowired
    private KafkaTemplate<String, DomainEvent> kafkaTemplate;

    @Value("${notification.topic.name}")
    private String notificationTopic;

    @Override
    public void send(DomainEvent event) {
        log.info("Event has been sent - {}", event);

        Headers headers = new RecordHeaders();
        headers.add("Test header", "Header value".getBytes());

        ProducerRecord<String, DomainEvent> record = new ProducerRecord<>(notificationTopic, null, "Notification", event, headers);
        kafkaTemplate.send(record);
    }
}
