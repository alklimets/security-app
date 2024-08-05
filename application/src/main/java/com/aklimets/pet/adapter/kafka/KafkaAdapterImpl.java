package com.aklimets.pet.adapter.kafka;

import com.aklimets.pet.buildingblock.interfaces.DomainEvent;
import com.aklimets.pet.domain.event.DomainEventAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class KafkaAdapterImpl implements DomainEventAdapter {

    @Override
    public void send(DomainEvent event) {
        log.info("Event has been sent - {}", event);
    }
}
