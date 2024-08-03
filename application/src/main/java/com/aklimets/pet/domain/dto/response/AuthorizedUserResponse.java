package com.aklimets.pet.domain.dto.response;

import java.util.Collection;

public record AuthorizedUserResponse(String id, String username, Collection<String> authorities) {
}
