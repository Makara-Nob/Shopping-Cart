package com.Myproject.ShoppingCart.Filter;

import com.Myproject.ShoppingCart.Models.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductSpec implements Specification<Product> {
    private final ProductFilter productFilter;

    @Override
    public Predicate toPredicate(Root<Product> product, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // If no filters are provided, return all products (no filtering)
        if (productFilter.getId() == null && !StringUtils.hasText(productFilter.getName())) {
            return criteriaBuilder.conjunction();  // This means no filtering, return all products
        }

        // List to hold predicates for the filtering conditions
        List<Predicate> predicates = new ArrayList<>();

        // Apply 'name' filter if it is provided
        if (StringUtils.hasText(productFilter.getName())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(product.get("name")),
                    "%" + productFilter.getName().toLowerCase() + "%"
            ));
        }

        // Apply 'id' filter if it is provided
        if (productFilter.getId() != null) {
            predicates.add(criteriaBuilder.equal(product.get("id"), productFilter.getId()));
        }

        // Combine all predicates (if any) using 'and'
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
