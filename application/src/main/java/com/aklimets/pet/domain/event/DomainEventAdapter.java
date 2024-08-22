package com.aklimets.pet.domain.event;


import com.aklimets.pet.event.DomainEvent;

public interface DomainEventAdapter {

    void send(DomainEvent event);
}
