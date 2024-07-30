package com.aklimets.pet.domain.payload;

import java.util.List;

public record ValidationPayload(String code, List<String> validationFails) {
}
