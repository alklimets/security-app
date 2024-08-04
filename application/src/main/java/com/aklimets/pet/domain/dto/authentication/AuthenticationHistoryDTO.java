package com.aklimets.pet.domain.dto.authentication;

import com.aklimets.pet.domain.model.authenticationhistory.attribute.IpAddress;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;

public record AuthenticationHistoryDTO (UserIdNumber userId, IpAddress ipAddress) {
}
