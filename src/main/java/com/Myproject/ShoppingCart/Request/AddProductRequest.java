package com.Myproject.ShoppingCart.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Inventory is required")
    @Positive(message = "Inventory must be greater than zero")
    private int inventory;

    @NotBlank(message = "Description is required")
    private String description;
    private Long categoryId;
    private String categoryName;
}
