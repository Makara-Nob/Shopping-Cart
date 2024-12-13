package com.Myproject.ShoppingCart.dto;

import com.Myproject.ShoppingCart.Models.Image;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ImageDto {
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
