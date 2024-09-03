package org.epam.gymapplication.exception;

import org.epam.gymapplication.domain.dto.ExceptionResponsePayload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = {ItemNotExistsException.class})
    public ResponseEntity<Object> handleItemNotExistsException(ItemNotExistsException e){
        logger.warn("404 [ItemNotExistsException] - {}", e.getMessage());
        ExceptionResponsePayload exceptionResponse = new ExceptionResponsePayload(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {JwtTokenExpiredException.class})
    public ResponseEntity<Object> handleJwtTokenExpiredException(JwtTokenExpiredException e){
        logger.warn("401 [JwtTokenExpiredException] - {}", e.getMessage());
        ExceptionResponsePayload exceptionResponse = new ExceptionResponsePayload(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {BadAuthenticationDataException.class})
    public ResponseEntity<Object> handleBadAuthDataException(BadAuthenticationDataException e){
        logger.warn("401 [BadAuthenticationDataException] - {}", e.getMessage());
        ExceptionResponsePayload exceptionResponse = new ExceptionResponsePayload(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {BlockedOperationException.class})
    public ResponseEntity<Object> handleBlockedOperationException(BlockedOperationException e){
        logger.warn("403 [BlockedOperationException] - {}", e.getMessage());
        ExceptionResponsePayload exceptionResponse = new ExceptionResponsePayload(HttpStatus.FORBIDDEN, e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleMissingParamException(MissingServletRequestParameterException e){
        logger.warn("400 [MissingRequiredParamException] - {}", e.getMessage());
        ExceptionResponsePayload exceptionResponse = new ExceptionResponsePayload(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        List<ExceptionResponsePayload> validationErrors = mapValidationErrorsToResponsePayload(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrors);
    }

    private List<ExceptionResponsePayload> mapValidationErrorsToResponsePayload( Map<String, String> errors) {
        return errors.entrySet()
                .stream()
                .map(error -> new ExceptionResponsePayload(
                        HttpStatus.BAD_REQUEST,
                        generateMessageForValidationIssue(error.getKey(), error.getValue()))).toList();
    }

    private String generateMessageForValidationIssue(String fieldName, String message) {
        return String.format("Issue in field '%s': %s", fieldName, message);
    }
}
