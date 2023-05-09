package com.fm.music.exception.advice;

import com.fm.music.exception.custom.*;
import com.fm.music.model.response.wrapper.ErrorResponsePayload;
import com.fm.music.model.response.wrapper.ValidationPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomNotFoundException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(CustomNotFoundException exception) {
        return withStatus(404, exception);
    }

    @ExceptionHandler(value = CustomUnauthorizedException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(CustomUnauthorizedException exception) {
        return withStatus(401, exception);
    }

    @ExceptionHandler(value = CustomForbiddenException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(CustomForbiddenException exception) {
        return withStatus(403, exception);
    }

    @ExceptionHandler(value = CustomServerErrorException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(CustomServerErrorException exception) {
        return withStatus(500, exception);
    }

    @ExceptionHandler(value = CustomBadRequestException.class)
    private ResponseEntity<ErrorResponsePayload> handleCustomException(CustomBadRequestException exception) {
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

    private ResponseEntity<ErrorResponsePayload> withStatus(int status, CustomRuntimeException e) {
        return ResponseEntity.status(status)
                .body(new ErrorResponsePayload(e.getErrorCode(), e.getErrorMessage()));
    }
}
