package com.aklimets.pet.domain.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends DefaultDomainRuntimeException {

    public UnauthorizedException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
