package org.week05lab01.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week05lab01.order.domain.Order;
import org.week05lab01.order.domain.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @GetMapping
    ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    ResponseEntity<Void> save(@RequestBody Order order) {
        service.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
