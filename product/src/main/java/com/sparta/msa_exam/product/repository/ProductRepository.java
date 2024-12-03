package com.sparta.msa_exam.product.repository;

import com.sparta.msa_exam.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    boolean existsById(Long productId);
}
