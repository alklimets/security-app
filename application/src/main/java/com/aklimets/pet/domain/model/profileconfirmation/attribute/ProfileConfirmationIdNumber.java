package com.aklimets.pet.domain.model.profileconfirmation.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfileConfirmationIdNumber extends DomainAttribute<String> {

    @Column(name = "id")
    @NotNull
    private String value;

    protected ProfileConfirmationIdNumber() {
    }

    public static ProfileConfirmationIdNumber generate() {
        return new ProfileConfirmationIdNumber(UUID.randomUUID().toString());
    }
}
