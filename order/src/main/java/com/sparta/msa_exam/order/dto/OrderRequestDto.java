package com.sparta.msa_exam.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    @NotNull(message = "상품 ID는 필수입니다.")
    private List<Long> productIds;
}
