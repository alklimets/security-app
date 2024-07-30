package com.aklimets.pet.domain.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public record UserDetailsRequestDTO (
    @NotBlank(message = "User details name cannot be blank") String name,
    @NotBlank(message = "User details surname cannot be blank") String surname,
    String country,
    String city) {
}
