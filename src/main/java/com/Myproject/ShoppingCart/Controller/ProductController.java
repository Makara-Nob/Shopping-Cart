package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Pagination.ProductResponse;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.Service.product.IProductService;
import com.Myproject.ShoppingCart.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("product")
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<?> getProduct(@RequestParam Map<String,String> params) {
        ProductResponse product = productService.getAllProduct(params);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
            var product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
   public ResponseEntity<?> addProduct(@RequestBody AddProductRequest request) {
           Product product = productService.addProduct(request);
           return ResponseEntity.status(HttpStatus.CREATED).body(product);
   }

    @PutMapping("/{Id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long Id, @RequestBody ProductUpdateRequest request) {
           ProductDto Product = productService.updateProduct(request, Id);
           return ResponseEntity.ok(Product);
   }

   @DeleteMapping("/{productId}")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
           productService.deleteProduct(productId);
           return ResponseEntity.noContent().build();
   }

    @GetMapping("/stock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> countProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
            var productCount = productService.countProductByBrandAndName(brandName,productName);
            return ResponseEntity.ok(productCount);
    }
}
