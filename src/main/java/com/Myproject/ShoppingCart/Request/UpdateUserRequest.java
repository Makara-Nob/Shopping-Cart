package com.Myproject.ShoppingCart.Request;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
public class UpdateUserRequest {
    private String first_name;
    private String Last_name;
}
