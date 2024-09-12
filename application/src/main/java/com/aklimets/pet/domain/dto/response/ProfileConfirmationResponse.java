package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationStatus;
import com.aklimets.pet.domain.model.user.attribute.AccountStatus;

public record ProfileConfirmationResponse(AccountStatus accountStatus) {
}
