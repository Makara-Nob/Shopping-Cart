package com.Myproject.ShoppingCart.Service.Category;

import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final ModelMapper mapper;

    @Override
    public Category getCategoryById(Long id) {
        logger.info("Executing: {}, Input: {}", getClass(), id);
        return categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public List<CategoryDto> getAllCategory() {
         List<Category> categories = categoryRepository.findByParentIsNull();
        return categories.stream()
                .map(this::ConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getSubCategories(Long parentId) {
         Category parent = categoryRepository.findById(parentId)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found"));
        List<Category> categories = categoryRepository.findByParent(parent);
        return List.of(mapper.map(categories, CategoryDto.class));
    }

    @Override
    public void addCategory(CategoryDto category) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());

        if(category.getParent() != null){
            Category parent = categoryRepository.findById(category.getId())
                    .orElseThrow(()-> new ResourceNotFoundException("Parent category not found"));

            newCategory.setParent(parent);
        }
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

    private CategoryDto ConvertToDto(Category category){
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getSubCategories()
                        .stream()
                        .map(this::ConvertToDto)
                        .collect(Collectors.toList())
        );
    }
}
