package org.week05lab01.orderDetail.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week05lab01.orderDetail.domain.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
