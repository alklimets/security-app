package com.aklimets.pet.domain.model.userprofile.attribute;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Embeddable
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class Address {

    @NotNull
    @Valid
    private Country country;

    @NotNull
    @Valid
    private City city;

    protected Address() {
    }
}
