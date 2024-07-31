package com.aklimets.pet.domain.dto.request;

import javax.validation.constraints.NotBlank;

public record JwtRefreshTokenRequestDTO(@NotBlank(message = "Refresh token cannot be blank") String refreshToken) {
}
