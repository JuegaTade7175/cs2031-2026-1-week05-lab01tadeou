package org.week05lab01.inventory.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week05lab01.inventory.domain.Inventory;
import org.week05lab01.inventory.domain.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService service;

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
}
