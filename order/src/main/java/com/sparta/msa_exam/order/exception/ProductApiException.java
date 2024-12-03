package com.sparta.msa_exam.order.exception;

public class ProductApiException extends RuntimeException {
    public ProductApiException(String message) {
        super(message);
    }
}
