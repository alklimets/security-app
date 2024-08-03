package com.aklimets.pet.domain.dto.response;

public record AuthenticationTokensResponse(String accessToken, String refreshToken) {
}
