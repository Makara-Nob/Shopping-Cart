package com.Myproject.ShoppingCart.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class createAddressRequest {
    private String street;        // Street or Village/Commune Name
    private String district;      // District
    private String province;
    private String postalCode;    // Optional postal code
    private String country;
}
