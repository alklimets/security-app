package com.aklimets.pet.domain.event;

import com.aklimets.pet.buildingblock.interfaces.DomainEvent;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationContent;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.model.security.EmailAddress;


public record DomainNotificationKafkaEvent(EmailAddress address,
                                           NotificationSubject subject,
                                           NotificationContent content) implements DomainEvent {

    @Override
    public String toString() {
        return "DomainNotificationKafkaEvent{" +
                "address=" + address.getValue() +
                ", subject=" + subject.getValue() +
                ", content=" + content .getValue()+
                '}';
    }
}
