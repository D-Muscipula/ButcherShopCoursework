package edu.java.shop.controller;

import edu.java.shop.domain.service.OrderService;
import edu.java.shop.dto.OrderDTO;
import edu.java.shop.dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Действия с заказами")
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    @Operation(summary = "Сделать заказ")
    public void makeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.makeOrder(orderRequest);
    }

    @GetMapping
    @Operation(summary = "Получить все заказы")
    public List<OrderDTO> getOrders() {
        return orderService.getOrders();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Отменить заказ")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Заплатить за заказ")
    public void payForOrder(@PathVariable("id") Long id) {
        orderService.payForOrder(id);
    }



}
