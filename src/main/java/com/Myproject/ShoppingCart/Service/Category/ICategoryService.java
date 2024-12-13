package com.Myproject.ShoppingCart.Service.Category;

import com.Myproject.ShoppingCart.Models.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    List<Category> getAllCategory();
    Category getCategoryByName(String name);
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);
}
