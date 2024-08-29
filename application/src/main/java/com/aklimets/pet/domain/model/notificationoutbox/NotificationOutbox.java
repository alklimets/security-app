package com.aklimets.pet.domain.model.notificationoutbox;

import com.aklimets.pet.domain.model.common.CreationTimestamp;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.*;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.model.attribute.RequestId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "notification_outbox", schema = "security")
@AllArgsConstructor
public class NotificationOutbox {

    @EmbeddedId
    private NotificationOutboxIdNumber id;

    @AttributeOverride(name = "value", column = @Column(name = "email"))
    private EmailAddress email;

    private NotificationSubject subject;

    private NotificationContent content;

    @Enumerated(EnumType.STRING)
    private OutboxProcessStatus status;

    @AttributeOverride(name = "value", column = @Column(name = "request_id"))
    private RequestId requestId;

    private EventType eventType;

    private CreationTimestamp timestamp;

    public NotificationOutbox() {
    }

    public void process() {
        this.status = OutboxProcessStatus.P;
    }

    public void fail() {
        this.status = OutboxProcessStatus.F;
    }
}
