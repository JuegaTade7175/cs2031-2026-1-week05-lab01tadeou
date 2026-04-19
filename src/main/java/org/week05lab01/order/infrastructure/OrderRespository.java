package org.week05lab01.order.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week05lab01.order.domain.Order;

public interface OrderRespository extends JpaRepository<Order, Long> {
}
