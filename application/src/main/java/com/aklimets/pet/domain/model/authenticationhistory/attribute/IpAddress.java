package com.aklimets.pet.domain.model.authenticationhistory.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IpAddress extends DomainAttribute<String> {

    @Column(name = "ip_address")
    @NotNull
    private String value;

    protected IpAddress() {
    }
}
