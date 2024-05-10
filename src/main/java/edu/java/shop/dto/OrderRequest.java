package edu.java.shop.dto;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(@NotNull String shippingAddress,
                           @NotNull String phoneNumber,
                           @NotNull String comment) {

}
