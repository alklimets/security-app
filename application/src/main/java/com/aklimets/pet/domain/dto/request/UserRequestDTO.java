package com.aklimets.pet.domain.dto.request;

import javax.validation.constraints.NotBlank;

public record UserRequestDTO (
    @NotBlank(message = "Username cannot be blank") String username,
    @NotBlank(message = "Password cannot be blank") String password){
}
