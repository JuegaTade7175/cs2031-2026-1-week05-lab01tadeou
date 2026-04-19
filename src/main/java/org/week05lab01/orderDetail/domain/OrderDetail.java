package org.week05lab01.orderDetail.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.week05lab01.order.domain.Order;
import org.week05lab01.product.domain.Product;

@Setter
@Getter
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private Integer quantity;

    private Double unitPrice;

    public OrderDetail() {
    }

    public OrderDetail(Long id, Order order, Product product, Integer quantity, Double unitPrice) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
