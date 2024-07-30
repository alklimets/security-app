package com.fm.music.model.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

public record JwtTokenPairRequestDTO (
        @NotBlank(message = "Access token cannot be blank") String accessToken,
        @NotBlank(message = "Refresh token cannot be blank") String refreshToken) {
}
