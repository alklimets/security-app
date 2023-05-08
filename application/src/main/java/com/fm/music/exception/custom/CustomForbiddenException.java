package com.fm.music.exception.custom;

import lombok.Getter;

@Getter
public class CustomForbiddenException extends CustomRuntimeException {

    public CustomForbiddenException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
