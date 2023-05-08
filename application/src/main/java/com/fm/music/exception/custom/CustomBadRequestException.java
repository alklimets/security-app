package com.fm.music.exception.custom;

import lombok.Getter;

@Getter
public class CustomBadRequestException extends CustomRuntimeException {

    public CustomBadRequestException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
