package org.week05lab01.inventory.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.week05lab01.product.domain.Product;

@Getter
public class InventoryUpdateEvent extends ApplicationEvent {
    private final Product product;

    private final Integer quantity;

    public InventoryUpdateEvent(Object source, Product product, Integer quantity) {
        super(source);
        this.product = product;
        this.quantity = quantity;
    }
}
