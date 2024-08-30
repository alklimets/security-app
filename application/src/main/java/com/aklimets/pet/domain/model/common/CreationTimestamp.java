package com.aklimets.pet.domain.model.common;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreationTimestamp extends DomainAttribute<LocalDateTime> {

    @Column(name = "timestamp")
    @NotNull
    private LocalDateTime value;

    protected CreationTimestamp() {
    }
}