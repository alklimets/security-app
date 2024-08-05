package com.aklimets.pet.domain.model.common;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CreationTimestamp implements DomainAttribute<LocalDateTime> {

    @Column(name = "timestamp")
    @NotNull
    private LocalDateTime value;

    protected CreationTimestamp() {
    }
}