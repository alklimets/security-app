package com.aklimets.pet.domain.event;

import com.aklimets.pet.buildingblock.interfaces.DomainEvent;

public interface DomainEventAdapter {

    void send(DomainEvent event);
}
