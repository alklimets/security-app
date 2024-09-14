package com.aklimets.pet.application.service.outbox;

import com.aklimets.pet.domain.dto.outbox.NotificationOutboxDTO;
import com.aklimets.pet.domain.dto.outbox.OutboxContentDTO;
import com.aklimets.pet.domain.event.DomainNotificationKafkaEvent;
import com.aklimets.pet.domain.event.attribute.NotificationContentMap;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutbox;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutboxFactory;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutboxRepository;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationContent;
import com.aklimets.pet.event.DomainEventAdapter;
import com.aklimets.pet.model.attribute.RequestId;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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

    public static final String REQUEST_ID = "requestId";

    private final DomainEventAdapter adapter;

    private final NotificationOutboxRepository outboxRepository;

    private final NotificationOutboxRepository notificationOutboxRepository;

    private final NotificationOutboxFactory notificationOutboxFactory;

    @Async("threadPoolTaskExecutor")
    public void processNotificationOutboxEvent() {
        outboxRepository.getFirstByStatusOrderByTimestamp(N).ifPresent(this::processOutbox);
    }

    private void processOutbox(NotificationOutbox outbox) {
        MDC.put(REQUEST_ID, outbox.getRequestId().getValue());
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

    @SneakyThrows
    public void postNotification(OutboxContentDTO content) {
        var outboxDto = new NotificationOutboxDTO(content.emailAddress(),
                content.subject(),
                new NotificationContent(new ObjectMapper().writeValueAsString(content.contentMap())),
                content.eventType(),
                new RequestId(MDC.get(REQUEST_ID)));
        var outboxEvent = notificationOutboxFactory.create(outboxDto);
        notificationOutboxRepository.save(outboxEvent);
    }
}
