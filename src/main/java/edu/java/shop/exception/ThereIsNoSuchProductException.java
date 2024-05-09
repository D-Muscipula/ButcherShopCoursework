package edu.java.shop.exception;

public class ThereIsNoSuchProductException extends RuntimeException{
    public ThereIsNoSuchProductException(String s) {
        super(s);
    }
}
