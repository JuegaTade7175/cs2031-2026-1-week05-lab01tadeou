package org.week05lab01.orderDetail.application;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}
