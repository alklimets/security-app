package com.aklimets.pet.application.service.outbox;

import com.aklimets.pet.domain.event.DomainNotificationKafkaEvent;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutbox;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutboxRepository;
import com.aklimets.pet.event.DomainEventAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            var domainNotificationEvent = new DomainNotificationKafkaEvent(
                    outbox.getEmail(),
                    outbox.getSubject(),
                    outbox.getContent(),
                    outbox.getRequestId()
            );
            adapter.send(domainNotificationEvent);
            outbox.process();
        } catch (Exception e) {
            outbox.fail();
            log.error("Notification processing has been failed. Cause: {}", e.getMessage());
        }
    }
}
