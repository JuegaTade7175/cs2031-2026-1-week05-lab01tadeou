package org.week05lab01.product.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.week05lab01.product.infrastructure.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public List<Product> listAll() {
        return repository.findAll();
    }

    public Product save(Product product) {
        return repository.save(product);
    }
}
