package edu.java.shop.exception;

public class ThereIsNoSuchItemInCartException extends RuntimeException{
    public ThereIsNoSuchItemInCartException(String s) {
        super(s);
    }
}
