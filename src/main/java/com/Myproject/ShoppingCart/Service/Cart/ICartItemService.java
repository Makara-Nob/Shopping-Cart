package com.Myproject.ShoppingCart.Service.Cart;

import com.Myproject.ShoppingCart.Models.CartItem;
import com.Myproject.ShoppingCart.dto.CartItemDto;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);

}
