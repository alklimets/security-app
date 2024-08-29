package com.aklimets.pet.application.service.outbox;

import com.aklimets.pet.domain.event.DomainNotificationKafkaEvent;
import com.aklimets.pet.domain.event.attribute.NotificationContentMap;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutbox;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutboxRepository;
import com.aklimets.pet.event.DomainEventAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.aklimets.pet.domain.model.notificationoutbox.attribute.OutboxProcessStatus.N;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class OutboxService {

    private final DomainEventAdapter adapter;

    private final NotificationOutboxRepository outboxRepository;

    @Async("threadPoolTaskExecutor")
    public void processNotificationOutboxEvent() {
        outboxRepository.getFirstByStatusOrderByTimestamp(N).ifPresent(this::processOutbox);
    }

    private void processOutbox(NotificationOutbox outbox) {
        MDC.put("requestId", outbox.getRequestId().getValue());
        try {
            Map<String, String> contentMap = new ObjectMapper().readValue(outbox.getContent().getValue(), new TypeReference<>() { });
            var domainNotificationEvent = new DomainNotificationKafkaEvent(
                    outbox.getEmail(),
                    outbox.getSubject(),
                    new NotificationContentMap(contentMap),
                    outbox.getRequestId()
            );
            adapter.send(domainNotificationEvent, outbox.getEventType().getValue());
            outbox.process();
        } catch (Exception e) {
            outbox.fail();
            log.error("Notification processing has been failed. Cause: {}", e.getMessage());
        }
    }
}
