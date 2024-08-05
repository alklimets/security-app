package com.aklimets.pet.domain.model.notificationoutbox;

import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationOutboxIdNumber;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.OutboxProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationOutboxRepository extends JpaRepository<NotificationOutbox, NotificationOutboxIdNumber> {

    Optional<NotificationOutbox> getFirstByStatusOrderByTimestamp(OutboxProcessStatus status);

}
