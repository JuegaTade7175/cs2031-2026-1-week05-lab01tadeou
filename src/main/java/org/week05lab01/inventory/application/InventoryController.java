package org.week05lab01.inventory.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week05lab01.inventory.domain.Inventory;
import org.week05lab01.inventory.domain.InventoryService;
import org.week05lab01.inventory.events.InventoryUpdateEvent;
import org.week05lab01.product.domain.Product;
import org.week05lab01.product.domain.ProductService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;
    private final ProductService productService;
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<List<Inventory>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/heavy")
    public ResponseEntity<Void> heavy(@RequestParam("ms") Long ms) {
        service.heavyProcess(ms);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Inventory inventory) {
        service.save(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/buyProduct")
    public ResponseEntity<Void> buyProduct(@RequestParam("productId") Long productId, @RequestParam("quantity") Integer quantity) {
        Product product = productService.findById(productId);
        publisher.publishEvent(new InventoryUpdateEvent(this, product, quantity));
        return ResponseEntity.accepted().build();
    }
}
