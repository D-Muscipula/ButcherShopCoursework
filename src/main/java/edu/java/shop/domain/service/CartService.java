package edu.java.shop.domain.service;

import edu.java.shop.domain.entity.Cart;
import edu.java.shop.domain.entity.CartItem;
import edu.java.shop.domain.entity.Product;
import edu.java.shop.domain.entity.User;
import edu.java.shop.domain.repository.CartRepository;
import edu.java.shop.domain.repository.ProductRepository;
import edu.java.shop.dto.CartItemDTO;
import edu.java.shop.dto.CartItemRequest;
import edu.java.shop.exception.ProductThereIsNoSuchQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final UserService userService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    @Transactional
    public void addItemToCart(CartItemRequest cartItemRequest) {
        Cart cart = getCart();
        List<CartItem> cartItems = cart.getItems();

        Product product = productRepository.getReferenceById(cartItemRequest.productId());
        if (product.getQuantityKg() < cartItemRequest.quantity()) {
            String message = "Количество товара " + product.getName()
                    + " составляет лишь" + product.getQuantityKg() + "кг";
            throw new ProductThereIsNoSuchQuantityException(message);
        }
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(cartItemRequest.quantity());
        cartItems.add(cartItem);
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
