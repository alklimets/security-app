package com.aklimets.pet.domain.dto.response;

public record AuthenticationTokensDTO (String accessToken, String refreshToken) {
}
