package com.sparta.msa_exam.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductRequestDto {
    @NotNull(message = "상품 이름은 필수입니다.")
    private String name;

    @NotNull(message = "상품 가격은 필수입니다.")
    @Min(value = 0, message = "가격은 양의 정수여야 합니다.")
    private Integer supply_Price;
}
