package com.fm.music.exception.custom;

import lombok.Getter;

@Getter
public class CustomServerErrorException extends CustomRuntimeException {

    public CustomServerErrorException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
