package org.week05lab01.orderDetail.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.week05lab01.orderDetail.domain.OrderDetail;

@Getter
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;

    public static OrderDetailResponse fromEntity(OrderDetail od) {
        return new OrderDetailResponse(
                od.getId(),
                od.getOrder().getId(),
                od.getProduct().getId(),
                od.getProduct().getName(),
                od.getQuantity(),
                od.getUnitPrice()
        );
    }
}
