package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Service.Cart.ICartItemService;
import com.Myproject.ShoppingCart.Service.Cart.ICartService;
import com.Myproject.ShoppingCart.Service.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("cartItems")
public class CartItemController {
    private final ICartItemService iCartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping
    public ResponseEntity<?> addItemToCart(@RequestParam Long productId,
                                           @RequestParam Integer quantity){
                User user = userService.getAuthenticatedUser();
                Cart cart = cartService.initializeNewCart(user);
            iCartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long cartId, @PathVariable Long itemId) {
            iCartItemService.removeItemFromCart(cartId,itemId);
            return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateItemQuantity(@RequestParam(required = false) Long cartId,
                                                @RequestParam Long itemId,
                                                @RequestParam Integer quantity) {
            iCartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok().build();
    }


}
