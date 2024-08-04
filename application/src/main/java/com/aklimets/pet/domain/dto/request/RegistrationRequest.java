package com.aklimets.pet.domain.dto.request;

import com.aklimets.pet.model.security.EmailAddress;
import com.aklimets.pet.model.security.Password;
import com.aklimets.pet.model.security.Username;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record RegistrationRequest(
    @Valid @NotNull(message = "Username could not be null") Username username,
    @Valid @NotNull(message = "Email could not be null") EmailAddress email,
    @Valid @NotNull(message = "Password could not be null") Password password,
    @Valid @NotNull(message = "Details could not be null") UserDetailsRequest details ) {
}
