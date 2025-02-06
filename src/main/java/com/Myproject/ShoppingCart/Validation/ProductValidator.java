package com.Myproject.ShoppingCart.Validation;

import com.Myproject.ShoppingCart.Exception.AlreadyExistsException;
import com.Myproject.ShoppingCart.Exception.ProductNotFoundException;
import com.Myproject.ShoppingCart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    public final ProductRepository productRepository;

    public void ValidateProductAlreadyExist(String brand, String name) {
        if(productRepository.existsByNameAndBrand(brand,name)) {
            throw new AlreadyExistsException("Brand: " + brand + ", Name: " + name);
        }
    }

    public void ValidateProductNotFound(Long id) {
        if(productRepository.existsById(id)){
            throw new ProductNotFoundException("Product with Id " + id + " not found!");
        }
    }
}
