package com.BallaDream.BallaDream.repository.product;

import com.BallaDream.BallaDream.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductName(String name);
}
