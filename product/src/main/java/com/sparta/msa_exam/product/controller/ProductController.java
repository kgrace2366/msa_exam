package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.ProductRequestDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    @Value("${server.port}")
    private String port;

    private final ProductService productService;

    @PostMapping
    @CacheEvict(value = "productList", allEntries = true)
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        Product createdProduct = productService.addProduct(productRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(createdProduct);
    }

    @GetMapping
    @CachePut(value = "productList")
    public ResponseEntity<List<Product>> getProducts() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        return ResponseEntity.ok().headers(headers).body(productService.getProducts());
    }

    @GetMapping("/{productId}/exists")
    public ResponseEntity<?> isProductExist(@PathVariable Long productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        if(productId <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유효하지 않은 상품 ID입니다.");
        }
        return ResponseEntity.ok().headers(headers).body(productService.existById(productId));
    }
}
