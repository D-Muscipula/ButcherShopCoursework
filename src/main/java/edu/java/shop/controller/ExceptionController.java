package edu.java.shop.controller;

import edu.java.shop.exception.UserAlreadyExists;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorMessage> handleException(UserAlreadyExists exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
