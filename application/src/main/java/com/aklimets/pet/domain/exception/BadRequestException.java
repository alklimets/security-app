package com.aklimets.pet.domain.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends DefaultDomainRuntimeException {

    public BadRequestException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
