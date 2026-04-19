package org.week05lab01.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.week05lab01.order.infrastructure.OrderRespository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRespository respository;

    public List<Order> list() {
        return respository.findAll();
    }

    public void save(Order order) {
        respository.save(order);
    }
}
