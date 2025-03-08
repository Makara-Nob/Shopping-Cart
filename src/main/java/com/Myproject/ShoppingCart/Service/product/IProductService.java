package com.Myproject.ShoppingCart.Service.product;

import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Pagination.PaginationResponse;
import com.Myproject.ShoppingCart.Pagination.ProductResponse;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IProductService {
    ProductDto addProduct(String clientRequest, List<MultipartFile> files) throws Exception;
    ProductResponse getAllProduct(Map<String,String> params);
    ProductDto getProductById(Long id);
    ProductDto updateProduct(ProductUpdateRequest request, Long id);
    void deleteProduct(Long id);
    Long countProductByBrandAndName(String brand, String name);
}
