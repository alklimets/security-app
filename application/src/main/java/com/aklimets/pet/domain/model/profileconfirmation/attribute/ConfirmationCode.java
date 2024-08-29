package com.aklimets.pet.domain.model.profileconfirmation.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfirmationCode extends DomainAttribute<String> {

    @Column(name = "confirmation_code")
    @NotNull
    @Size(min = 1, max = 50, message = "Content size should be between 1 and 50 characters")
    private String value;

    protected ConfirmationCode() {
    }

    public static ConfirmationCode generate() {
        return new ConfirmationCode(UUID.randomUUID().toString());
    }
}
