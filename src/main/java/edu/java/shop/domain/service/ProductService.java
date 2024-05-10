package edu.java.shop.domain.service;

import edu.java.shop.domain.entity.Product;
import edu.java.shop.domain.repository.ProductRepository;
import edu.java.shop.dto.ProductRequest;
import edu.java.shop.exception.ThereIsNoSuchProductException;
import edu.java.shop.exception.ThisProductAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    @Transactional
    public void addProduct(ProductRequest productRequest) {
        Optional<Product> product = productRepository.getOptionalProductByName(productRequest.name());
        if (product.isPresent()) {
            throw new ThisProductAlreadyExistsException("Продукт " + productRequest.name() + " уже есть");
        }
        Product newProduct = new Product();
        newProduct.setName(productRequest.name());
        newProduct.setDescription(productRequest.description());
        newProduct.setPrice(productRequest.price());
        newProduct.setQuantityKg(productRequest.quantityKg());

        productRepository.save(newProduct);
    }

    @Transactional
    public void changePrice(Long id, double price) {
        Optional<Product> product = productRepository.getOptionalProductById(id);
        if (product.isEmpty()) {
            throw new ThereIsNoSuchProductException("Продукта под id "
                    + id + " " + " нет");
        }
        Product changed = product.get();
        if (price > 0) {
            changed.setPrice(price);
            productRepository.save(changed);
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void changeQuantity(Long id, Double quantity) {
        Optional<Product> product = productRepository.getOptionalProductById(id);
        if (product.isEmpty()) {
            throw new ThereIsNoSuchProductException("Продукта под id "
                    + id + " " + " нет");
        }
        Product changed = product.get();
        if (quantity < 0) {
            changed.setQuantityKg(0);
        } else {
            changed.setQuantityKg(quantity);
        }
        productRepository.save(changed);
    }

    public void addQuantity(Long id, Double quantity) {
        Optional<Product> product = productRepository.getOptionalProductById(id);
        if (product.isEmpty()) {
            throw new ThereIsNoSuchProductException("Продукта под id "
                    + id + " " + " нет");
        }
        Product changed = product.get();
        if (quantity > 0) {
            changed.setQuantityKg(changed.getQuantityKg() + quantity);
            productRepository.save(changed);
        }
    }
}
