package com.aklimets.pet.domain.model.notificationoutbox.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class NotificationContent implements DomainAttribute<String> {

    @Column(name = "content")
    @NotNull
    @Size(min = 1, max = 5000, message = "Content size should be between 1 and 5000 characters")
    private String value;

    protected NotificationContent() {
    }
}
