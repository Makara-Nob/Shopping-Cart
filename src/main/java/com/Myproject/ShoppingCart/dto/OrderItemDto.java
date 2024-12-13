package com.Myproject.ShoppingCart.dto;

import com.Myproject.ShoppingCart.Models.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
    private ProductDto product;
}
