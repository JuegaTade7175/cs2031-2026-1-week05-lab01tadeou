package org.week05lab01.order.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.week05lab01.order.domain.Order;

@Getter
public class NewOrderEvent extends ApplicationEvent {
    private final Order order;

    public NewOrderEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}
