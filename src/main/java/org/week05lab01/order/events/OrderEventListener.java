package org.week05lab01.order.events;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @EventListener
    @Async
    public void handleNewOrderEvent(NewOrderEvent event) {
        System.out.println("Enviando correo para la orden: " + event.getOrder().getId());
    }
}