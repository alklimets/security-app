package com.aklimets.pet.domain.dto.request;

import com.aklimets.pet.domain.model.userprofile.attribute.City;
import com.aklimets.pet.domain.model.userprofile.attribute.Country;
import com.aklimets.pet.domain.model.userprofile.attribute.Name;
import com.aklimets.pet.domain.model.userprofile.attribute.Surname;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserDetailsRequest(
    @Valid @NotNull(message = "User profile name cannot be null") Name name,
    @Valid @NotBlank(message = "User profile surname cannot be null") Surname surname,
    @Valid @NotNull(message = "User profile country cannot be null")Country country,
    @Valid @NotNull(message = "User profile city cannot be null")City city) {
}
