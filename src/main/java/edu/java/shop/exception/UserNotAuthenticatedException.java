package edu.java.shop.exception;

public class UserNotAuthenticatedException extends RuntimeException{
    public UserNotAuthenticatedException(String s) {
        super(s);
    }
}
