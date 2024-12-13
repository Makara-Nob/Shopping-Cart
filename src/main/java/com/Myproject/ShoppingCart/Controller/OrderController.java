package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Order;
import com.Myproject.ShoppingCart.Response.ApiResponse;
import com.Myproject.ShoppingCart.Service.Order.IOrderService;
import com.Myproject.ShoppingCart.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService iOrderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order orders = iOrderService.placeOrder(userId);
            OrderDto orderDto = iOrderService.convertToDto(orders);
            return ResponseEntity.ok(new ApiResponse("Success!",orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occurred",e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order/by-orderId")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order =  iOrderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Success", order));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/{userId}/order/by-userId")
    public ResponseEntity<ApiResponse> getOrderByUserId(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = iOrderService.getUserOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Success!",orders));
        } catch (ResourceNotFOundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


}
