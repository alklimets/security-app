package com.aklimets.pet.domain.dto.response;

import java.util.Collection;

public record AuthorizedUserResponseDTO(String id, String username, Collection<String> authorities) {
}
