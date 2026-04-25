package org.week05lab01.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week05lab01.order.domain.Order;
import org.week05lab01.order.domain.OrderService;
import org.week05lab01.order.events.NewOrderEvent;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Order order) {
        Order savedOrder = service.save(order);
        publisher.publishEvent(new NewOrderEvent(this, savedOrder));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}