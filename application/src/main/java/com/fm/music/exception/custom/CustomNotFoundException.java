package com.fm.music.exception.custom;

import lombok.Getter;

@Getter
public class CustomNotFoundException extends CustomRuntimeException {

    public CustomNotFoundException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
