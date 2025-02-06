package com.Myproject.ShoppingCart.Filter;

import com.Myproject.ShoppingCart.Models.Product;
import io.micrometer.common.lang.NonNullApi;
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

    List<Predicate> predicates = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<Product> product, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (StringUtils.hasText(productFilter.getName())){
            Predicate names = criteriaBuilder.like(criteriaBuilder.lower(product.get("name")), "%" + productFilter.getName().toLowerCase() + "%");
            predicates.add(names);
        }

        if (productFilter.getId() != null){
            Predicate id = product.get("id").in(productFilter.getId());
            predicates.add(id);
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
