package edu.java.shop.domain.service;

import edu.java.shop.domain.entity.*;
import edu.java.shop.domain.repository.CartItemRepository;
import edu.java.shop.domain.repository.OrderItemRepository;
import edu.java.shop.domain.repository.OrderRepository;
import edu.java.shop.domain.repository.ProductRepository;
import edu.java.shop.dto.OrderDTO;
import edu.java.shop.dto.OrderItemDTO;
import edu.java.shop.dto.OrderRequest;
import edu.java.shop.exception.OrderCannotBeCancelledException;
import edu.java.shop.exception.OrderCannotBePaidException;
import edu.java.shop.exception.ProductThereIsNoSuchQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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


    @Transactional
    public void cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Проверить, что статус заказа не "Выполнен"
            if (order.getStatus() != OrderStatus.CLOSED) {
                // Вернуть товары обратно на склад
                List<OrderItem> orderItems = order.getItems();
                for (OrderItem orderItem : orderItems) {
                    Product product = orderItem.getProduct();
                    product.setQuantityKg(product.getQuantityKg() + orderItem.getQuantity());
                }

                // Удалить заказ из базы данных
                orderRepository.delete(order);
            } else {
                throw new OrderCannotBeCancelledException("Невозможно отменить выполненный заказ");
            }
        } else {
            throw new OrderCannotBeCancelledException("Заказ с идентификатором " + orderId + " не найден");
        }
    }


    public List<OrderDTO> getOrders() {
        List<Order> orders = orderRepository.getOrdersByUserId(userService.getCurrentUser().getId());
        List<OrderDTO> result = new ArrayList<>();
        for (var order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderId(order.getId());

            List<OrderItemDTO> orderItemDTOS = getOrderItemDTOS(orderItems);
            OrderDTO orderDTO = new OrderDTO(order.getId(), orderItemDTOS, order.getCreationDate(), order.getLastModifiedDate(), order.getStatus().name());
            result.add(orderDTO);
        }
        return result;
    }

    private static List<OrderItemDTO> getOrderItemDTOS(List<OrderItem> orderItems) {
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (var orderItem : orderItems) {
            OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem.getId(),
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity() * orderItem.getProduct().getPrice(),
                    orderItem.getQuantity());
            orderItemDTOS.add(orderItemDTO);
        }
        return orderItemDTOS;
    }

    public Cart getCart() {
        User user = userService.getCurrentUser();
        return user.getCart();
    }

    @Transactional
    public void changeOldOrders(Long intervalSinceLastCheck, OrderStatus orderStatus) {
        for (var order : orderRepository.findAllByStatus(orderStatus)) {
            Date date = new Date();
            //если заказ не оплачен за адекватное время, он отменяется
            if (date.getTime() - order.getLastModifiedDate().getTime() > intervalSinceLastCheck) {
                cancelOrder(order.getId());
            }
        }
    }

    public void payForOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getStatus().equals(OrderStatus.NEW)) {
                order.setStatus(OrderStatus.PAID);
                orderRepository.save(order);
            } else {
                throw new OrderCannotBePaidException("Заказ уже был оплачен");
            }
        } else {
            throw new OrderCannotBePaidException("Заказ с идентификатором " + id + " не найден");
        }
    }
}
