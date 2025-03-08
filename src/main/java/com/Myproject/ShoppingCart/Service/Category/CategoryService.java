package com.Myproject.ShoppingCart.Service.Category;

import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
import com.Myproject.ShoppingCart.Mapper.CategoryMapper;
import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Repository.CategoryRepository;
import com.Myproject.ShoppingCart.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper mapper;
    private final CategoryMapper categoryMapper;

    @Override
    public Category getCategoryById(Long id) {
        logger.info("Executing: {}, Input: {}", getClass(), id);
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public List<CategoryDto> getAllCategory() {
         List<Category> categories = categoryRepository.findAll();
        return categoryMapper.convertCategoryToListDto(categories);
    }

    @Override
    public void addCategory(CategoryDto category) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        categoryRepository.save(newCategory);
    }

    @Override
    public void updateCategory(CategoryDto category, Long id) {
        Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            oldCategory.setName(category.getName());
             return categoryRepository.save(oldCategory);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void deleteCategory(Long id) {
            categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, ()->{
                throw new ResourceNotFoundException("Category not found!");
            });
    }
}
