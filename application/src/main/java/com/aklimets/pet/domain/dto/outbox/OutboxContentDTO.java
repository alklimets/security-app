package com.aklimets.pet.domain.dto.outbox;

import com.aklimets.pet.domain.model.notificationoutbox.attribute.EventType;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.model.attribute.EmailAddress;

import java.util.Map;

public record OutboxContentDTO (EmailAddress emailAddress,
                                NotificationSubject subject,
                                Map<String,String> contentMap,
                                EventType eventType) {
}
