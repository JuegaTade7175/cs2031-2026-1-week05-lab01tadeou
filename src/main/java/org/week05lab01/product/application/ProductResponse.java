package org.week05lab01.product.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.week05lab01.product.domain.Product;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;

    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
