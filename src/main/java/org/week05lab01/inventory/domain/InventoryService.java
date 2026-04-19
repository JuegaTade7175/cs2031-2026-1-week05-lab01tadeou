package org.week05lab01.inventory.domain;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.week05lab01.inventory.infrastructure.InventoryRepository;
import org.week05lab01.product.domain.Product;

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

        heavyProcess(60000L);

        // TODO: Handle exceptions
        inventory.setStock(inventory.getStock() - quantity);

        repository.save(inventory);
    }
}
