package org.week05lab01.inventory.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.week05lab01.inventory.infrastructure.InventoryRepository;
import org.week05lab01.product.domain.Product;
import org.week05lab01.shared.exception.InsufficientStockException;
import org.week05lab01.shared.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public List<Inventory> list() {
        return repository.findAll();
    }

    public void save(Inventory inventory) {
        repository.save(inventory);
    }

    public void heavyProcess(Long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception exception) {
            logger.error("Ocurrió un problema con el thread secundario: {}", exception.getMessage());
        }
    }

    public void reduceStock(Product product, Integer quantity) {
        Inventory inventory = repository.findByProductId(product.getId());

        if (inventory == null) {
            throw new ResourceNotFoundException(
                    "No se encontró inventario para el producto con id: " + product.getId()
            );
        }

        if (inventory.getStock() < quantity) {
            throw new InsufficientStockException(
                    "Stock insuficiente para el producto '" + product.getName() +
                    "'. Stock disponible: " + inventory.getStock() + ", cantidad solicitada: " + quantity
            );
        }

        heavyProcess(60000L);

        inventory.setStock(inventory.getStock() - quantity);
        repository.save(inventory);

        logger.info("Stock reducido correctamente para producto '{}'. Stock restante: {}",
                product.getName(), inventory.getStock());
    }
}
