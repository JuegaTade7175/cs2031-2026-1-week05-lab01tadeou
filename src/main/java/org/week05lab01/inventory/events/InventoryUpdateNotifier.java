package org.week05lab01.inventory.events;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.week05lab01.inventory.domain.InventoryService;
import org.week05lab01.shared.exception.InsufficientStockException;
import org.week05lab01.shared.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class InventoryUpdateNotifier {
    private final InventoryService service;

    private final Logger logger = LoggerFactory.getLogger(InventoryUpdateNotifier.class);

    @EventListener
    @Async
    public void processInventoryUpdate(InventoryUpdateEvent event) {
        try {
            service.reduceStock(event.getProduct(), event.getQuantity());
            // Notificación de éxito
            logger.info("[NOTIFICACIÓN] Inventario actualizado correctamente para el producto '{}'.",
                    event.getProduct().getName());
        } catch (ResourceNotFoundException ex) {
            logger.error("[NOTIFICACIÓN] Error al actualizar inventario: {}", ex.getMessage());
        } catch (InsufficientStockException ex) {
            logger.warn("[NOTIFICACIÓN] Stock insuficiente: {}", ex.getMessage());
        } catch (Exception ex) {
            logger.error("[NOTIFICACIÓN] Error inesperado al procesar actualización de inventario: {}",
                    ex.getMessage());
        }
    }
}
