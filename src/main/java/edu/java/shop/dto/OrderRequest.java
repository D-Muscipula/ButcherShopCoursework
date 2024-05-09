package edu.java.shop.dto;

public record OrderRequest(String shippingAddress,
                           String phoneNumber,
                           String comment) {

}
