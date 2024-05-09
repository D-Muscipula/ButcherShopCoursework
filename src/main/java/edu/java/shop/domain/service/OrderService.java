package edu.java.shop.domain.service;

import edu.java.shop.domain.entity.*;
import edu.java.shop.domain.repository.*;
import edu.java.shop.dto.OrderDTO;
import edu.java.shop.dto.OrderItemDTO;
import edu.java.shop.dto.OrderRequest;
import edu.java.shop.exception.ProductThereIsNoSuchQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserService userService;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public void makeOrder(OrderRequest orderRequest) {
        //проверить dto
        Cart cart = getCart();
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            return;
        }
        Order order = new Order();
        order.setUser(userService.getCurrentUser());
        order.setStatus(OrderStatus.NEW);
        order.setPhoneNumber(orderRequest.phoneNumber());
        order.setShippingAddress(orderRequest.shippingAddress());
        order.setComment(orderRequest.comment());
        order.setItems(new ArrayList<>());
        order = orderRepository.save(order);
        List<OrderItem> orderItems = order.getItems();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();

            Product product = cartItem.getProduct();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);

            if (cartItem.getQuantity() > product.getQuantityKg()) {
                String message = "Количество товара " + product.getName()
                        + " составляет лишь " + product.getQuantityKg() + " кг";
                throw new ProductThereIsNoSuchQuantityException(message);
            }

            product = productRepository.getReferenceById(product.getId());
            product.setQuantityKg(product.getQuantityKg() - cartItem.getQuantity());
            cartItemRepository.deleteById(cartItem.getId());
        }
        cartItems.clear();
    }



    public List<OrderDTO> getOrders() {
        List<Order> orders = orderRepository.getOrdersByUserId(userService.getCurrentUser().getId());
        List<OrderDTO> result = new ArrayList<>();
        for (var order: orders) {
            List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderId(order.getId());

            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
            for (var orderItem: orderItems) {
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem.getId(),
                        orderItem.getProduct().getName(),
                        orderItem.getQuantity() * orderItem.getProduct().getPrice(),
                        orderItem.getQuantity());
                orderItemDTOS.add(orderItemDTO);
            }
            OrderDTO orderDTO = new OrderDTO(orderItemDTOS, order.getCreationDate(), order.getLastModifiedDate(), order.getStatus().name());
            result.add(orderDTO);
        }
        return result;
    }

    public Cart getCart() {
        User user = userService.getCurrentUser();
        return user.getCart();
    }
}
