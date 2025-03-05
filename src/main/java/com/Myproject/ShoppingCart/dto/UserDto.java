package com.Myproject.ShoppingCart.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String first_name;
    private String last_name;
    private String email;
}
