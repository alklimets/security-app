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
@EqualsAndHashCode(callSuper = true)
public class NotificationSubject extends DomainAttribute<String> {

    @Column(name = "subject")
    @NotNull
    @Size(min = 1, max = 500, message = "Subject size should be between 1 and 500 characters")
    private String value;

    protected NotificationSubject() {
    }
}
