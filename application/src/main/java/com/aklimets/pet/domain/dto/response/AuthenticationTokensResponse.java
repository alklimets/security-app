package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.jwt.model.attribute.AccessToken;
import com.aklimets.pet.jwt.model.attribute.RefreshToken;

public record AuthenticationTokensResponse(AccessToken accessToken, RefreshToken refreshToken) {
}
