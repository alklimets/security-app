package com.aklimets.pet.domain.dto.outbox;

import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationContent;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.model.security.EmailAddress;

public record NotificationOutboxDTO (EmailAddress emailAddress,
                                    NotificationSubject subject,
                                    NotificationContent content) {
}
