package com.aklimets.pet.domain.dto.outbox;

import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationContent;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.model.attribute.RequestId;

public record NotificationOutboxDTO (EmailAddress emailAddress,
                                     NotificationSubject subject,
                                     NotificationContent content,
                                     RequestId requestId) {
}
