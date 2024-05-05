package edu.java.shop.exception;

public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists(String s) {
        super(s);
    }
}
