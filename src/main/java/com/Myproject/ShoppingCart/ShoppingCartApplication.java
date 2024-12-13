package com.Myproject.ShoppingCart;

import com.Myproject.ShoppingCart.Security.JWT.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
		JwtUtils jwtUtils;
	}

}
