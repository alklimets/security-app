package com.aklimets.pet.adapter.schedule;

import com.aklimets.pet.application.service.outbox.OutboxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Transactional
@AllArgsConstructor
public class NotificationOutboxEventScheduler {

    private final OutboxService outboxService;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void processNotificationOutboxEvent() {
        outboxService.processNotificationOutboxEvent();
    }
}
