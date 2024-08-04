package com.aklimets.pet.domain.model.authenticationhistory.attribute;

import com.aklimets.pet.buildingblock.interfaces.DomainAttribute;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class IpAddress implements DomainAttribute<String> {

    @Column(name = "ip_address")
    @NotNull
    private String value;

    protected IpAddress() {
    }
}
