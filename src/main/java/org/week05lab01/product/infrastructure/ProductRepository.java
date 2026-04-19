package org.week05lab01.product.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.week05lab01.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
