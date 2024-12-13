package com.Myproject.ShoppingCart.Service.Cart;

import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.dto.CartDto;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

    CartDto convertToDto(Cart cart);
}
