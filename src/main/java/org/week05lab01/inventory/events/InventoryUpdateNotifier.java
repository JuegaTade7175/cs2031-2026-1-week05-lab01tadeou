package org.week05lab01.inventory.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.week05lab01.inventory.domain.InventoryService;

@Component
@RequiredArgsConstructor
public class InventoryUpdateNotifier {
    private final InventoryService service;

    @EventListener
    @Async
    public void processInventoryUpdate(InventoryUpdateEvent event) {
        // TODO: Handle exceptions and notify users
        service.reduceStock(event.getProduct(), event.getQuantity());
    }
}
