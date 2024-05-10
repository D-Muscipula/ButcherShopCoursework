package edu.java.shop.domain.repository;

import edu.java.shop.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> getOptionalProductById(Long id);
    Optional<Product> getOptionalProductByName(String name);
}
