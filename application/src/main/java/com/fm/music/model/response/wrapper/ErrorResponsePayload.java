package com.fm.music.model.response.wrapper;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ErrorResponsePayload", description = "Error response model")
public record ErrorResponsePayload(String errorCode, String errorMessage) {
}
