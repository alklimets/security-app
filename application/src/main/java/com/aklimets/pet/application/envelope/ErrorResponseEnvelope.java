package com.aklimets.pet.application.envelope;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ErrorResponseEnvelope", description = "Error response envelope")
public record ErrorResponseEnvelope(String errorCode, String errorMessage) {
}
