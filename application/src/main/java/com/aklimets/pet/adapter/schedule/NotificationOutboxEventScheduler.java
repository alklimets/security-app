package com.aklimets.pet.adapter.schedule;

import com.aklimets.pet.domain.event.DomainEventAdapter;
import com.aklimets.pet.domain.event.DomainNotificationKafkaEvent;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutbox;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutboxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.aklimets.pet.domain.model.notificationoutbox.attribute.OutboxProcessStatus.N;

@Component
@Slf4j
@Transactional
@AllArgsConstructor
public class NotificationOutboxEventScheduler {

    private final NotificationOutboxRepository outboxRepository;

    private final DomainEventAdapter adapter;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void processNotificationOutboxEvent() {
        outboxRepository.getFirstByStatusOrderByTimestamp(N).ifPresent(this::processEvent);
    }

    private void processEvent(NotificationOutbox notificationOutbox) {
        try {
            var domainNotificationEvent = new DomainNotificationKafkaEvent(
                    notificationOutbox.getEmail(),
                    notificationOutbox.getSubject(),
                    notificationOutbox.getContent(),
                    notificationOutbox.getRequestId()
            );
            adapter.send(domainNotificationEvent);
            notificationOutbox.process();
        } catch (Exception e) {
            notificationOutbox.fail();
            log.error("Notification processing has been failed. Cause: {}", e.getMessage());
        }
    }
}
