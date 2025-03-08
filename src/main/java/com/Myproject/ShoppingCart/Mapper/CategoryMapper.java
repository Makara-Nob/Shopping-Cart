package com.Myproject.ShoppingCart.Mapper;

import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "id",target = "categoryId")
    CategoryDto convertCategoryToDto(Category category);
    List<CategoryDto> convertCategoryToListDto(List<Category> category);
}
