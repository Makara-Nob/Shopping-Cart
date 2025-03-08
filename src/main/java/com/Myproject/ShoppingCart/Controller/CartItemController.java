package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Response.ApiResponse;
import com.Myproject.ShoppingCart.Service.Cart.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("cart/items")
public class CartItemController {

    private final ICartItemService iCartItemService;

    @PostMapping
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam Long cartId,
            @RequestParam Long productId,
            @RequestParam int quantity
    ){
        iCartItemService.addItemToCart(cartId,productId,quantity);
        ApiResponse response = new ApiResponse("success","item added successfully",null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> removeItem(@RequestParam Long cartId,
                                        @RequestParam Long itemId) {
        iCartItemService.removeItemFromCart(cartId,itemId);
        ApiResponse response = new ApiResponse("success","item removed successfully",null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateItemQuantity(
            @RequestParam Long cartId,
            @RequestParam Long itemId,
            @RequestParam int quantity
    ) throws BadRequestException
    {
        iCartItemService.updateItemQuantity(cartId,itemId,quantity);
        ApiResponse response = new ApiResponse("success","item updated successfully",null);
        return ResponseEntity.ok(response);
    }
}