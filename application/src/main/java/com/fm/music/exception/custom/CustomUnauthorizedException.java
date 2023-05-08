package com.fm.music.exception.custom;

import lombok.Getter;

@Getter
public class CustomUnauthorizedException extends CustomRuntimeException {

    public CustomUnauthorizedException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
