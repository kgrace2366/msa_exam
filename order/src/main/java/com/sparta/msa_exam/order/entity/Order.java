package com.sparta.msa_exam.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> products;

    // 필수 값 초기화 위한 생성자
    public Order(List<OrderProduct> productMappings) {
        this.products = productMappings;
        this.products.forEach(product -> product.setOrder(this));
    }

    // 상품 추가 메서드
    public void addProduct(OrderProduct orderProduct) {
        products.add(orderProduct);
        orderProduct.setOrder(this);  // 양방향 관계 설정
    }
}
