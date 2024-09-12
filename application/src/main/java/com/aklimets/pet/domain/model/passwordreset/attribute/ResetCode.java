package com.aklimets.pet.domain.model.passwordreset.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResetCode extends DomainAttribute<String> {

    @Column(name = "reset_code")
    @NotNull
    @Size(min = 1, max = 150, message = "Content size should be between 1 and 150 characters")
    private String value;

    protected ResetCode() {
    }
}
