package com.aklimets.pet.controller.advice;

import com.aklimets.pet.domain.exception.*;
import com.aklimets.pet.domain.payload.ErrorResponsePayload;
import com.aklimets.pet.domain.payload.ValidationPayload;
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
    private ResponseEntity<ErrorResponsePayload> handleCustomException(NotFoundException exception) {
        return withStatus(404, exception);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(UnauthorizedException exception) {
        return withStatus(401, exception);
    }

    @ExceptionHandler(value = JwtException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(JwtException exception) {
        return withStatus(401, new UnauthorizedException("Jwt token is not valid", exception.getMessage()));
    }

    @ExceptionHandler(value = ForbiddenException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(ForbiddenException exception) {
        return withStatus(403, exception);
    }

    @ExceptionHandler(value = ServerErrorException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(ServerErrorException exception) {
        return withStatus(500, exception);
    }

    @ExceptionHandler(value = BadRequestException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(BadRequestException exception) {
        return withStatus(400, exception);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseEntity<ValidationPayload> handleException(MethodArgumentNotValidException exception) {
        List<String> payload = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(422)
                .body(new ValidationPayload("Validation failed", payload));
    }

    @ExceptionHandler(value = Exception.class)
    private ResponseEntity<ErrorResponsePayload> handleException(Exception e) {
        return ResponseEntity.status(500)
                .body(new ErrorResponsePayload("Internal server error", e.getMessage()));
    }

    private ResponseEntity<ErrorResponsePayload> withStatus(int status, DefaultDomainRuntimeException e) {
        return ResponseEntity.status(status)
                .body(new ErrorResponsePayload(e.getErrorCode(), e.getErrorMessage()));
    }
}
