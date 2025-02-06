package com.Myproject.ShoppingCart.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String street;
    private String district;
    private String province;
    private String postalCode;
    private String country;
}
