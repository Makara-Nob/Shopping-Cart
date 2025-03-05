package com.Myproject.ShoppingCart.dto;

import com.Myproject.ShoppingCart.Models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long productId;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private List<ImageDto> images;
}
