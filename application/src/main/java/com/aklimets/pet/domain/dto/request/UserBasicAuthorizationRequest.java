package com.aklimets.pet.domain.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public record UserBasicAuthorizationRequest (
    @NotBlank(message = "Username cannot be blank") String username,
    @NotBlank(message = "Password cannot be blank") String password) {
}
