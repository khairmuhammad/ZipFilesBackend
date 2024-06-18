package com.khair.zipfilesbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles IOException thrown by the application.
     *
     * This method is triggered whenever an IOException is thrown within any controller method.
     * It catches the exception and returns a ResponseEntity with a 400 Bad Request status and
     * the exception message as the response body.
     *
     * @param ex The IOException that was thrown.
     * @return A ResponseEntity with a 400 Bad Request status and the exception message.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
