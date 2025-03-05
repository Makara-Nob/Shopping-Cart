package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Pagination.ProductResponse;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.Service.product.IProductService;
import com.Myproject.ShoppingCart.dto.ProductDto;
import com.Myproject.ShoppingCart.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("product")
public class ProductController {

    private final IProductService productService;

    /**
     * Get all products with pagination.
     * @param params search parameters for pagination
     * @return a response with status, message, and product data
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getProduct(@RequestParam Map<String, String> params) {
        ProductResponse productResponse = productService.getAllProduct(params);
        ApiResponse response = new ApiResponse("success", "Products retrieved successfully", productResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get a specific product by its ID.
     * @param productId the ID of the product
     * @return a response with status, message, and product data
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        ProductDto product = productService.getProductById(productId);
        ApiResponse response = new ApiResponse("success", "Product retrieved successfully", product);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Add a new product with image uploads.
     * @param request product details in JSON format
     * @param files product images
     * @return a response with status, message, and created product data
     * @throws Exception if there is any issue with file upload or saving
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addProduct(@RequestPart("product") String request,
                                                  @RequestPart List<MultipartFile> files) throws Exception {
        ProductDto product = productService.addProduct(request, files);
        ApiResponse response = new ApiResponse("success", "Product created successfully", product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing product.
     * @param id the product ID to update
     * @param request product update details
     * @return a response with status, message, and updated product data
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        ProductDto updatedProduct = productService.updateProduct(request, id);
        ApiResponse response = new ApiResponse("success", "Product updated successfully", updatedProduct);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product by its ID.
     * @param productId the ID of the product to delete
     * @return a response indicating the deletion status
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        ApiResponse response = new ApiResponse("success", "Product deleted successfully", null);
        return ResponseEntity.noContent().build();
    }

    /**
     * Count products by brand and name.
     * @param brandName the brand name to filter by
     * @param productName the product name to filter by
     * @return a response with status, message, and product count data
     */
    @GetMapping("/stock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        long productCount = productService.countProductByBrandAndName(brandName, productName);
        ApiResponse response = new ApiResponse("success", "Product count retrieved successfully", productCount);
        return ResponseEntity.ok(response);
    }

}
