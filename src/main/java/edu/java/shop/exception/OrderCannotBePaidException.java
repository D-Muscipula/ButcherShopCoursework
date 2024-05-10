package edu.java.shop.exception;

public class OrderCannotBePaidException extends RuntimeException{
    public OrderCannotBePaidException(String s) {
        super(s);
    }
}
