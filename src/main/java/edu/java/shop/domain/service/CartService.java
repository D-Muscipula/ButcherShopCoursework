package edu.java.shop.domain.service;

import edu.java.shop.domain.entity.Cart;
import edu.java.shop.domain.entity.CartItem;
import edu.java.shop.domain.entity.Product;
import edu.java.shop.domain.entity.User;
import edu.java.shop.domain.repository.CartItemRepository;
import edu.java.shop.domain.repository.ProductRepository;
import edu.java.shop.dto.CartItemDTO;
import edu.java.shop.dto.CartItemRequest;
import edu.java.shop.exception.ProductThereIsNoSuchQuantityException;
import edu.java.shop.exception.ThereIsNoSuchItemInCartException;
import edu.java.shop.exception.ThereIsNoSuchProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserService userService;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;


    @Transactional
    public void addItemToCart(CartItemRequest cartItemRequest) {
        CartItemRequest modified = new CartItemRequest(cartItemRequest.productId(), cartItemRequest.quantity());
        Cart cart = getCart();
        List<CartItem> cartItems = cart.getItems();
        Optional<Product> productOptional = productRepository.getOptionalProductById(cartItemRequest.productId());
        if (productOptional.isEmpty()) {
            throw new ThereIsNoSuchProductException("Продукта под id "
                    + cartItemRequest.productId() + " " + " нет");
        }
        Product product = productOptional.get();

        //Если товар уже есть в корзине
        int ind = -1;
        CartItem existingCartItem = null;
        for (int i = 0; i < cartItems.size(); i++) {
            if (Objects.equals(cartItems.get(i).getProduct().getId(), cartItemRequest.productId())) {
                existingCartItem = cartItems.get(i);
                ind = i;
            }
        }

        if (existingCartItem != null){
            modified = new CartItemRequest(cartItemRequest.productId(),
                    existingCartItem.getQuantity() + cartItemRequest.quantity());
        }
        if (product.getQuantityKg() < modified.quantity()) {
            String message = "Количество товара " + product.getName()
                    + " составляет лишь " + product.getQuantityKg() + " кг";
            throw new ProductThereIsNoSuchQuantityException(message);
        }
        if (existingCartItem == null) {
            existingCartItem = new CartItem();
            existingCartItem.setProduct(product);
            existingCartItem.setCart(cart);
        }
        existingCartItem.setQuantity(modified.quantity());
        if (ind != - 1) {
            cartItems.set(ind, existingCartItem);
        } else {
            cartItems.add(existingCartItem);
        }

    }

    @Transactional
    public void deleteSomeQuantityOfItemFromCart(CartItemRequest cartItemRequest) {
        CartItemRequest modified;
        Cart cart = getCart();
        List<CartItem> cartItems = cart.getItems();
        Optional<Product> productOptional = productRepository.getOptionalProductById(cartItemRequest.productId());
        if (productOptional.isEmpty()) {
            throw new ThereIsNoSuchProductException("Продукта под id "
                    + cartItemRequest.productId() + " " + " нет");
        }

        int ind = -1;
        CartItem existingCartItem = null;
        for (int i = 0; i < cartItems.size(); i++) {
            if (Objects.equals(cartItems.get(i).getProduct().getId(), cartItemRequest.productId())) {
                existingCartItem = cartItems.get(i);
                ind = i;
            }
        }

        if (existingCartItem == null){
            throw new ThereIsNoSuchItemInCartException("В корзине нет такого товара");
        }
        double dif = existingCartItem.getQuantity() - cartItemRequest.quantity();
        if (dif < 0) {
            dif = 0;
        }
        if (dif == 0) {
            deleteItem(existingCartItem.getProduct().getId());
        } else {
            modified = new CartItemRequest(cartItemRequest.productId(), dif);
            existingCartItem.setQuantity(modified.quantity());
            cartItems.set(ind, existingCartItem);
        }
    }

    @Transactional
    public void deleteItem(Long productId) {
        Cart cart = getCart();
        List<CartItem> cartItems = cart.getItems();
        int ind = -1;
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(productId)) {
                ind = i;
            }
        }
        if (ind == -1) {
            throw new ThereIsNoSuchItemInCartException("В корзине нет такого товара");
        }
        cartItemRepository.deleteById(cartItems.get(ind).getId());
        cartItems.remove(ind);

    }

    public List<CartItemDTO> getCartItems() {
        Cart cart = getCart();
        return cart.getItems()
                .stream()
                .map((cartItem -> new CartItemDTO(cartItem.getId(), cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice() * cartItem.getQuantity(), cartItem.getQuantity())))
                .collect(Collectors.toList());
    }

    public Cart getCart() {
        User user = userService.getCurrentUser();
        return user.getCart();
    }
}
