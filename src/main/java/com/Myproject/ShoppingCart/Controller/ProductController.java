package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.Response.ApiResponse;
import com.Myproject.ShoppingCart.Service.product.IProductService;
import com.Myproject.ShoppingCart.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/product")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        List<Product> products = productService.getAllProduct();
        List<ProductDto> convertedProducts = productService.convertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success!",convertedProducts));
    }

    @GetMapping("/{productId}/All")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            var product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Success!",productDto));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @PostMapping("/add/product")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
   public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
       try {
           Product theProduct = productService.addProduct(product);
           return ResponseEntity.ok(new ApiResponse("Add Product Success!", theProduct));
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
       }
   }

    @PutMapping("/product/{Id}/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long Id, @RequestBody ProductUpdateRequest request) {
       try {
           Product theProduct = productService.updateProduct(request, Id);
           return ResponseEntity.ok(new ApiResponse("Update Product Success!", theProduct));
       } catch (ResourceNotFOundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
       }
   }

   @DeleteMapping("/product/{productId}/delete")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
       try {
           productService.deleteProduct(productId);
           return ResponseEntity.ok(new ApiResponse("Delete product Success!",productId));
       } catch (ResourceNotFOundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
       }
   }

   @GetMapping("/products/by/brand-and-name")
   public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
       try {
           List<Product> products = productService.getProductByBrandAndName(brandName, productName);
           List<ProductDto> productDtos = productService.convertedProducts(products);
           if(products.isEmpty()) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products Found!",null));
           }
           return ResponseEntity.ok(new ApiResponse("Success!", productDtos));
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
       }
   }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String categoryName, @RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductByCategoryAndBrand(categoryName,brandName);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            if(products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products Found!",null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/products/by-name/{productName}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String productName) {
        try {
            List<Product> products = productService.getProductByName(productName);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            if(products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products Found!",null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductByBrand(brandName);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            if(products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products Found!",null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/products/by-category/{categoryName}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductByCategory(categoryName);
            List<ProductDto> productDtos = productService.convertedProducts(products);
            if(products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No Products Found!",null));
            }
            return ResponseEntity.ok(new ApiResponse("Success!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/products/count/by-brand-and-name")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            var productCount = productService.countProductByBrandAndName(brandName,productName);

            return ResponseEntity.ok(new ApiResponse("Product count!",productCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
