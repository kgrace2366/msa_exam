package com.sparta.msa_exam.order.client;

import com.sparta.msa_exam.order.exception.ProductApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductClientFallback implements ProductClient {
    private static final Logger logger = LoggerFactory.getLogger(ProductClientFallback.class);
    @Override
    public boolean isProductExist(Long productId) {
        throw new ProductApiException("잠시 후에 주문 추가를 요청해 주세요.");
    }
}