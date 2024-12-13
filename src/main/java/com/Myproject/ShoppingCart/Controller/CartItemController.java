package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Response.ApiResponse;
import com.Myproject.ShoppingCart.Service.Cart.ICartItemService;
import com.Myproject.ShoppingCart.Service.Cart.ICartService;
import com.Myproject.ShoppingCart.Service.User.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService iCartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/add-Items")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity){
        try {
                User user = userService.getAuthenticatedUser();
                Cart cart = cartService.initializeNewCart(user);
            iCartItemService.addItemToCart(cart.getId(),productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Add item Success!",null));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            iCartItemService.removeItemFromCart(cartId,itemId);
            return ResponseEntity.ok(new ApiResponse("remove item Success!",null));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/item/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam(required = false) Long cartId,
                                                          @RequestParam Long itemId,
                                                          @RequestParam Integer quantity) {
        try {
            iCartItemService.updateItemQuantity(cartId,itemId,quantity);
            return ResponseEntity.ok(new ApiResponse("Update Success!",null));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }


}
