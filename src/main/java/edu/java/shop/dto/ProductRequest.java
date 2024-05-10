package edu.java.shop.dto;

import jakarta.validation.constraints.NotNull;

public record ProductRequest(@NotNull String name,
                             @NotNull String description,
                             @NotNull double price,
                             double quantityKg) {
}
