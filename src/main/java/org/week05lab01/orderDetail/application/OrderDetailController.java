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
    ResponseEntity<List<OrderDetail>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping
    ResponseEntity<Void> save(@RequestBody OrderDetail orderDetail) {
        service.save(orderDetail);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // TODO: Exceptions and DTOs
}
