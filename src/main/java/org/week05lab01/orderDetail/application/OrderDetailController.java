package org.week05lab01.orderDetail.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week05lab01.orderDetail.domain.OrderDetail;
import org.week05lab01.orderDetail.domain.OrderDetailService;

import java.util.List;

@RestController
@RequestMapping("/orders/detail")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService service;

    @GetMapping
    ResponseEntity<List<OrderDetailResponse>> list() {
        List<OrderDetailResponse> details = service.list()
                .stream()
                .map(OrderDetailResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(details);
    }

    @PostMapping
    ResponseEntity<OrderDetailResponse> save(@RequestBody OrderDetailRequest request) {
        OrderDetail saved = service.save(
                request.getOrderId(),
                request.getProductId(),
                request.getQuantity(),
                request.getUnitPrice()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderDetailResponse.fromEntity(saved));
    }
}
