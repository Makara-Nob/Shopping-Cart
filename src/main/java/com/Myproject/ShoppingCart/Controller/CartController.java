package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Response.ApiResponse;
import com.Myproject.ShoppingCart.Service.Cart.CartService;
import com.Myproject.ShoppingCart.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        CartDto cartDto = cartService.convertToDto(cart);
        ApiResponse response = new ApiResponse("success","Cart retrieved successfully",cartDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable Long cartId) {
            Cart cart = cartService.getCart(cartId);
            CartDto cartDto = cartService.convertToDto(cart);
            return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        ApiResponse response = new ApiResponse("success","Cart clear successfully",null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}/total-Price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        ApiResponse response = new ApiResponse("success","Total-Amount retrieved successfully",totalPrice);
        return ResponseEntity.ok(response);
    }
}
