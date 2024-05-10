package edu.java.shop.exception;

public class ThisProductAlreadyExistsException extends RuntimeException{
    public ThisProductAlreadyExistsException(String s) {
        super(s);
    }
}
