package com.aklimets.pet.domain.event;

import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationContent;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.event.RequestableDomainEvent;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.model.attribute.RequestId;


public record DomainNotificationKafkaEvent(EmailAddress address,
                                           NotificationSubject subject,
                                           NotificationContent content,
                                           RequestId requestId) implements RequestableDomainEvent<RequestId> {

    @Override
    public String toString() {
        return "DomainNotificationKafkaEvent{" +
                "address=" + address.getValue() +
                ", subject=" + subject.getValue() +
                ", content=" + content.getValue() +
                ", requestId=" + requestId.getValue() +
                '}';
    }

    @Override
    public RequestId getRequestId() {
        return requestId;
    }
}
