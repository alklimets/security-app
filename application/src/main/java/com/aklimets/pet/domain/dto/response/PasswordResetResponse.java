package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.domain.model.common.ResetStatus;

public record PasswordResetResponse(ResetStatus status) {
}
