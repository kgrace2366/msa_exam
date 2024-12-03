package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.MessageResponseDto;
import com.sparta.msa_exam.order.dto.OrderRequestDto;
import com.sparta.msa_exam.order.dto.OrderResponseDto;
import com.sparta.msa_exam.order.dto.ProductRequestDto;
import com.sparta.msa_exam.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    @Value("${server.port}")
    private String port;

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        orderService.addOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(new MessageResponseDto("Order Created"));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId,
                                                   @RequestBody @Valid ProductRequestDto productRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        orderService.updateOrder(orderId, productRequestDto);
        return ResponseEntity.ok().headers(headers).body(new MessageResponseDto("Order Updated"));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        OrderResponseDto response = orderService.getOrderById(orderId);
        return ResponseEntity.ok().headers(headers).body(response);
    }
}
