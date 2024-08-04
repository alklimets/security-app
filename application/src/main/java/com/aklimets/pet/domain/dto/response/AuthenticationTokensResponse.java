package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.buildingblock.interfaces.ResponseData;
import com.aklimets.pet.model.security.AccessToken;
import com.aklimets.pet.model.security.RefreshToken;

public record AuthenticationTokensResponse(AccessToken accessToken, RefreshToken refreshToken) implements ResponseData {
}
