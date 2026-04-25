package org.week05lab01.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String client;

    public Order() {
    }

    public Order(Long id, String client) {
        this.id = id;
        this.client = client;
    }
}
