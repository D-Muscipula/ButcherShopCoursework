package edu.java.shop.controller;

import edu.java.shop.domain.entity.Product;
import edu.java.shop.domain.service.ProductService;
import edu.java.shop.dto.ProductRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Действия с товарами")
public class ProductController {
    private final ProductService productService;

    @PutMapping
    @Operation(summary = "Добавление нового продукта")
    @PreAuthorize("hasRole('ADMIN')")
    public void addNewProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
    }

    @PostMapping("/price/{id}/{price}")
    @Operation(summary = "Изменение цены продукта")
    @PreAuthorize("hasRole('ADMIN')")
    public void changeProductPrice(@PathVariable("id") Long id, @PathVariable("price") Double price) {
        productService.changePrice(id, price);
    }

    @PostMapping("/quantity/{id}/{quantity}")
    @Operation(summary = "Изменение количества продукта")
    @PreAuthorize("hasRole('ADMIN')")
    public void changeProductQuantity(@PathVariable("id") Long id, @PathVariable("quantity") Double quantity) {
        productService.changeQuantity(id, quantity);
    }

    @PostMapping("/quantity/add/{id}/{quantity}")
    @Operation(summary = "Добавление продукта")
    @PreAuthorize("hasRole('ADMIN')")
    public void addProductQuantity(@PathVariable("id") Long id, @PathVariable("quantity") Double quantity) {
        productService.addQuantity(id, quantity);
    }

    @GetMapping
    @Operation(summary = "Получение всех продуктов")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
