package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.ProductRequestDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.entity.OrderProduct;
import com.sparta.msa_exam.order.exception.ProductApiException;
import com.sparta.msa_exam.order.exception.ResourceNotFoundException;
import com.sparta.msa_exam.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Transactional
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackGetProduct")
    public Order addOrder(OrderRequestDto orderRequestDto) {
        if (orderRequestDto.getProductIds() == null || orderRequestDto.getProductIds().isEmpty()) {
            throw new ResourceNotFoundException("상품 ID 목록이 비어있습니다.");
        }

        for (Long productId : orderRequestDto.getProductIds()) {
            boolean isProductExist = productClient.isProductExist(productId);
            if (!isProductExist) {
                throw new ResourceNotFoundException("존재하지 않는 상품 ID(" + productId + ") 입니다.");
            }
        }

        List<OrderProduct> productMappings = orderRequestDto.getProductIds().stream()
                .map(OrderProduct::new)
                .toList();

        Order order = new Order(productMappings);
        productMappings.forEach(product -> product.setOrder(order));

        return orderRepository.save(order);
    }

    public Order fallbackGetProduct(OrderRequestDto orderRequestDto, Throwable t) {
        throw new ProductApiException("잠시 후에 주문 추가를 요청해 주세요.");
    }

    @Transactional
    public Order updateOrder(Long orderId, ProductRequestDto productRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 주문이 존재하지 않습니다."));

        boolean isProductExist = productClient.isProductExist(productRequestDto.getProductId());
        if (!isProductExist) {
            throw new ResourceNotFoundException("존재하지 않는 상품 ID(" + productRequestDto.getProductId() + ") 입니다.");
        }

        OrderProduct orderProduct = new OrderProduct(productRequestDto.getProductId());
        order.addProduct(orderProduct);

        return orderRepository.save(order);
    }

    @Transactional
    @Cacheable(value = "orders", key = "#orderId", unless = "#result == null")
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("주문 ID(" + orderId + ")를 찾을 수 없습니다."));

        List<Long> productIds = order.getProducts().stream()
                .map(OrderProduct::getProductId)
                .toList();

        return new OrderResponseDto(order.getOrderId(), productIds);
    }
}
