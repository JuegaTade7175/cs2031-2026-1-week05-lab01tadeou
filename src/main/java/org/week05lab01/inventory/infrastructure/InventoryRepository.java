package org.week05lab01.inventory.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week05lab01.inventory.domain.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductId(Long id);
}
