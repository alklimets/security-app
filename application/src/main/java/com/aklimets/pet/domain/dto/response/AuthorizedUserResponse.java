package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.buildingblock.interfaces.ResponseData;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.model.attribute.Username;

import java.util.Collection;

public record AuthorizedUserResponse(UserIdNumber id, Username username, Collection<String> authorities) implements ResponseData {
}
