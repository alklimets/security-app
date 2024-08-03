package com.aklimets.pet.domain.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RegistrationRequest(
    @NotBlank(message = "Username cannot be blank") String username,
    @NotBlank(message = "Password cannot be blank") @Size(min = 8, max = 30, message = "Password doesn't match length requirements") String password,
    @NotNull(message = "Details could not be null") @Valid UserDetailsRequest details ) {
}
