package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Response.ApiResponse;
import com.Myproject.ShoppingCart.Service.Category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/Category")
public class CategoryController {
    private final ICategoryService iCategoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategory() {
        try {
            List<Category> categories = iCategoryService.getAllCategory();
            return ResponseEntity.ok(new ApiResponse("Found!",categories));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/addCategory")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        Category newCategory = iCategoryService.addCategory(category);
        return ResponseEntity.ok(new ApiResponse("Category created successfully!", category));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {

        try {
            Category theCategory = iCategoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Category Found!", theCategory));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category theCategory = iCategoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Category Found!", theCategory));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        try {
                Category updateCategory = iCategoryService.updateCategory(category, categoryId);
                return ResponseEntity.ok(new ApiResponse("Category updated successfully!", updateCategory));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        try {
                iCategoryService.deleteCategory(categoryId);
                return ResponseEntity.ok(new ApiResponse("Category deleted successfully!", null));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }
}
