package com.Myproject.ShoppingCart.Filter;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class ProductFilter {
    private Long id;
    private String name;

    public static ProductFilter buildProductFilter(Map<String, String> params) {
        ProductFilter productFilter = new ProductFilter();

        if (params.containsKey("name")) {
            productFilter.setName(params.get("name"));
        }

        if (params.containsKey("id")) {
            productFilter.setId(Long.parseLong(params.get("id")));
        }

        return productFilter;
    }
}