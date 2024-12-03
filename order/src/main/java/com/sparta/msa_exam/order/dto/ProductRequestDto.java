package com.sparta.msa_exam.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductRequestDto {
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;
}
