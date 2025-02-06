package com.Myproject.ShoppingCart.Service.product;

import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Pagination.ProductResponse;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    ProductResponse getAllProduct(Map<String,String> params);
    ProductDto getProductById(Long id);
    ProductDto updateProduct(ProductUpdateRequest request, Long id);
    void deleteProduct(Long id);
    Long countProductByBrandAndName(String brand, String name);
    List<ProductDto> convertedProducts(List<Product> products);
    ProductDto convertToDto(Product product);
}
