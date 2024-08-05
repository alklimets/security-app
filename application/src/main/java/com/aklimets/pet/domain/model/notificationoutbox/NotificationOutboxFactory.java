package com.aklimets.pet.domain.model.notificationoutbox;

import com.aklimets.pet.domain.dto.outbox.NotificationOutboxDTO;
import com.aklimets.pet.domain.model.common.CreationTimestamp;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationOutboxIdNumber;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.OutboxProcessStatus;
import com.aklimets.pet.util.datetime.TimeSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class NotificationOutboxFactory {

    private final TimeSource timeSource;

    public NotificationOutbox create(NotificationOutboxDTO dto) {
        return new NotificationOutbox(NotificationOutboxIdNumber.generate(),
                dto.emailAddress(),
                dto.subject(),
                dto.content(),
                OutboxProcessStatus.N,
                new CreationTimestamp(timeSource.getCurrentLocalDateTime()));
    }
}
