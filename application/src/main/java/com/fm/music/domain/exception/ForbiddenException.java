package com.fm.music.domain.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends DefaultDomainRuntimeException {

    public ForbiddenException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
