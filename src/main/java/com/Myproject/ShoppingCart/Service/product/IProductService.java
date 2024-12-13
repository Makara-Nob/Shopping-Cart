package com.Myproject.ShoppingCart.Service.product;

import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.dto.ProductDto;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product getProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long id);
    void deleteProduct(Long id);
    List<Product> getAllProduct();
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category, String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand, String name);
    Long countProductByBrandAndName(String brand, String name);

    List<ProductDto> convertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
