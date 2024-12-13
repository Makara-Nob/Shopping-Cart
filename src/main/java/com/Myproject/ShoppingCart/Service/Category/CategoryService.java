package com.Myproject.ShoppingCart.Service.Category;

import com.Myproject.ShoppingCart.Exception.AlreadyExistsException;
import com.Myproject.ShoppingCart.Exception.ProductNotFoundException;
import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Override
    public Category getCategoryById(Long id) {
        logger.info("Executing: " + getClass() + ", Input: " + id);
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFOundException("Category not found!"));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save).orElseThrow(() -> new AlreadyExistsException(category.getName()+" already exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
            return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFOundException("Category not found!"));
    }

    @Override
    public void deleteCategory(Long id) {
            categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, ()->{
                throw new ResourceNotFOundException("Category not found!");
            });
    }
}
