package backend.academy.scrapper.controller;

import backend.academy.common.dto.ApiErrorResponse;
import backend.academy.scrapper.exception.NotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String NOT_FOUND_MESSAGE = "Not found";
    private static final String BAD_REQUEST_MESSAGE = "Bad request";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleChatNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(
                        NOT_FOUND_MESSAGE,
                        String.valueOf(HttpStatus.NOT_FOUND.value()),
                        ex.getClass().getSimpleName(),
                        ex.getMessage(),
                        Arrays.stream(ex.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(
                        BAD_REQUEST_MESSAGE,
                        String.valueOf(HttpStatus.BAD_REQUEST.value()),
                        ex.getClass().getSimpleName(),
                        ex.getMessage(),
                        Arrays.stream(ex.getStackTrace())
                                .map(StackTraceElement::toString)
                                .toList()));
    }
}
