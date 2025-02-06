package com.Myproject.ShoppingCart.dto;

import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Models.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private Long parent;
    private List<CategoryDto> subCategories = new ArrayList<>();
}
