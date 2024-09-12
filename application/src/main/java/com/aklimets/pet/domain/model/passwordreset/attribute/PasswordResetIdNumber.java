package com.aklimets.pet.domain.model.passwordreset.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PasswordResetIdNumber extends DomainAttribute<String> {

    @Column(name = "id")
    @NotNull
    private String value;

    protected PasswordResetIdNumber() {
    }

    public static PasswordResetIdNumber generate() {
        return new PasswordResetIdNumber(UUID.randomUUID().toString());
    }
}
