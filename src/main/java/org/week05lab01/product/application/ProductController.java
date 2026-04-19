package org.week05lab01.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.week05lab01.product.domain.Product;
import org.week05lab01.product.domain.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        List<Product> products = service.listAll();

        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Product product) {
        service.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // TODO: Exceptions and DTOs
}
