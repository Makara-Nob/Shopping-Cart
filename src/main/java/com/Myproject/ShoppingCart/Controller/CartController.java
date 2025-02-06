package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Service.Cart.CartService;
import com.Myproject.ShoppingCart.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
            Cart cart = cartService.getCartByUserId(userId);
            CartDto cartDto = cartService.convertToDto(cart);
            return ResponseEntity.ok(cartDto);
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
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/total-Price")
    public ResponseEntity<?> getTotalAmount(@PathVariable Long cartId) {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(totalPrice);
    }
}
