package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.exception.DuplicateResourceException;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product addProduct(ProductRequestDto productRequestDto) {
        productRepository.findByName(productRequestDto.getName())
                .ifPresent(product -> {
                    throw new DuplicateResourceException("이미 존재하는 상품입니다: " + productRequestDto.getName());
                });

        Product product = Product.builder()
                .name(productRequestDto.getName())
                .supply_price(productRequestDto.getSupply_Price())
                .build();

        return productRepository.save(product);
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public boolean existById(Long productId) {
        return productRepository.existsById(productId);
    }
}
