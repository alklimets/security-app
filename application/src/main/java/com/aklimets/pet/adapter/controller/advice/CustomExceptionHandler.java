package com.aklimets.pet.adapter.controller.advice;

import com.aklimets.pet.domain.exception.*;
import com.aklimets.pet.model.envelope.ErrorResponseEnvelope;
import com.aklimets.pet.model.envelope.ValidationEnvelope;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    private ResponseEntity<ErrorResponseEnvelope> handleCustomException(NotFoundException exception) {
        return withStatus(404, exception);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    private ResponseEntity<ErrorResponseEnvelope> handleCustomException(UnauthorizedException exception) {
        return withStatus(401, exception);
    }

    @ExceptionHandler(value = JwtException.class)
    private ResponseEntity<ErrorResponseEnvelope> handleCustomException(JwtException exception) {
        return withStatus(401, new UnauthorizedException("Jwt token is not valid", exception.getMessage()));
    }

    @ExceptionHandler(value = ForbiddenException.class)
    private ResponseEntity<ErrorResponseEnvelope> handleCustomException(ForbiddenException exception) {
        return withStatus(403, exception);
    }

    @ExceptionHandler(value = ServerErrorException.class)
    private ResponseEntity<ErrorResponseEnvelope> handleCustomException(ServerErrorException exception) {
        return withStatus(500, exception);
    }

    @ExceptionHandler(value = BadRequestException.class)
    private ResponseEntity<ErrorResponseEnvelope> handleCustomException(BadRequestException exception) {
        return withStatus(400, exception);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseEntity<ValidationEnvelope> handleException(MethodArgumentNotValidException exception) {
        logException(exception);
        List<String> payload = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(422)
                .body(new ValidationEnvelope("Validation failed", payload));
    }

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity<ErrorResponseEnvelope> handleException(Exception exception) {
        logException(exception);
        return ResponseEntity.status(500)
                .body(new ErrorResponseEnvelope("Internal server error", exception.getMessage()));
    }

    private ResponseEntity<ErrorResponseEnvelope> withStatus(int status, DefaultDomainRuntimeException exception) {
        logException(exception);
        return ResponseEntity.status(status)
                .body(new ErrorResponseEnvelope(exception.getErrorCode(), exception.getErrorMessage()));
    }

    private static void logException(Exception exception) {
        log.error("Exception occurred: {}", exception.getMessage());
    }
}
