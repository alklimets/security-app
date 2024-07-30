package com.fm.music.domain.exception;

import lombok.Getter;

@Getter
public class ServerErrorException extends DefaultDomainRuntimeException {

    public ServerErrorException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
