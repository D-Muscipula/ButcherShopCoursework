package edu.java.shop.exception;

public class OrderCannotBeCancelledException extends RuntimeException{
    public OrderCannotBeCancelledException(String s) {
        super(s);
    }
}
