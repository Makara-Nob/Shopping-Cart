package com.Myproject.ShoppingCart.Service.Category;

import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    List<CategoryDto> getAllCategory();
    void addCategory(CategoryDto category);
    void updateCategory(CategoryDto category, Long id);
    void deleteCategory(Long id);
}
