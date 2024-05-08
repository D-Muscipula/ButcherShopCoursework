package edu.java.shop.exception;

public class ProductThereIsNoSuchQuantityException extends RuntimeException{
    public ProductThereIsNoSuchQuantityException(String s) {
        super(s);
    }
}
