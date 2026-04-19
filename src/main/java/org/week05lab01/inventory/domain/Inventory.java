package org.week05lab01.inventory.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.week05lab01.product.domain.Product;

@Setter
@Getter
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    private int stock;

    public Inventory() {
    }

    public Inventory(Long id, Product product, int stock) {
        this.id = id;
        this.product = product;
        this.stock = stock;
    }
}
