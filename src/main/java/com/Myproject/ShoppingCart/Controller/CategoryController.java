package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Service.Category.ICategoryService;
import com.Myproject.ShoppingCart.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("category")
public class CategoryController {
    private final ICategoryService iCategoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
            List<CategoryDto> categories = iCategoryService.getAllCategory();
            return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto category) {
        iCategoryService.addCategory(category);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
            Category theCategory = iCategoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(theCategory);
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<?> getSubCategories(@PathVariable Long parentId) {
            List<CategoryDto> subCategories = iCategoryService.getSubCategories(parentId);
            return ResponseEntity.ok(subCategories);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto category) {
                iCategoryService.updateCategory(category, categoryId);
                return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
                iCategoryService.deleteCategory(categoryId);
                return ResponseEntity.noContent().build();
    }
}
