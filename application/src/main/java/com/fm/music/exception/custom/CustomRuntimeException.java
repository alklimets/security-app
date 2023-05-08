package com.fm.music.exception.custom;

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException{

    private final String errorCode;
    private final String errorMessage;

    public CustomRuntimeException(String errorCode, String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
