package edu.java.shop.controller;

import edu.java.shop.exception.*;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ProductThereIsNoSuchQuantityException.class)
    public ResponseEntity<ErrorMessage> handleProductThereIsNoSuchQuantityException(ProductThereIsNoSuchQuantityException exception) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ThereIsNoSuchProductException.class)
    public ResponseEntity<ErrorMessage> handleThereIsNoSuchProductException(ThereIsNoSuchProductException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ThereIsNoSuchItemInCartException.class)
    public ResponseEntity<ErrorMessage> handleThereIsNoSuchItemInCartException(ThereIsNoSuchItemInCartException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(ThisProductAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleThisProductAlreadyExistsException(ThisProductAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(OrderCannotBeCancelledException.class)
    public ResponseEntity<ErrorMessage> handleOrderCannotBeCancelledException(OrderCannotBeCancelledException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(OrderCannotBePaidException.class)
    public ResponseEntity<ErrorMessage> handleOrderCannotBePaidException(OrderCannotBePaidException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorMessage> handleUserNotAuthenticatedException(UserNotAuthenticatedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
