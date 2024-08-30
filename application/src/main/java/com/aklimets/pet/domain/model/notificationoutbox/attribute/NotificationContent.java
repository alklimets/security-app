package com.aklimets.pet.domain.model.notificationoutbox.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationContent extends DomainAttribute<String> {

    @Column(name = "content")
    @NotNull
    @Size(min = 1, max = 5000, message = "Content size should be between 1 and 5000 characters")
    private String value;

    protected NotificationContent() {
    }
}
