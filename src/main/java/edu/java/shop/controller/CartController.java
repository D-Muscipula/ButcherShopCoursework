package edu.java.shop.controller;

import edu.java.shop.domain.service.CartService;
import edu.java.shop.dto.CartItemDTO;
import edu.java.shop.dto.CartItemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Действия с корзиной")
public class CartController {
    private final CartService cartService;
    @PostMapping
    @Operation(summary = "Добавление в корзину")
    public void addToCart(@RequestBody CartItemRequest cartItemRequest) {
        cartService.addItemToCart(cartItemRequest);
    }

    @DeleteMapping
    @Operation(summary = "Удаление из корзины определенного количества")
    public void deleteSomeQuantityOfItemFromCart(@RequestBody CartItemRequest cartItemRequest) {
        cartService.deleteSomeQuantityOfItemFromCart(cartItemRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление из корзины элемента'")
    public void deleteItemFromCart(@PathVariable("id")Long id) {
        cartService.deleteItem(id);
    }

    @GetMapping
    @Operation(summary = "Получение корзины")
    public List<CartItemDTO> getCart() {
        return cartService.getCartItems();
    }
}
