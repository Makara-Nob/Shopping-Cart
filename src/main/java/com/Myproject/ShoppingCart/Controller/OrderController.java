package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Service.Order.IOrderService;
import com.Myproject.ShoppingCart.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("orders")
public class OrderController {
    private final IOrderService iOrderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam Long userId,
                                         @RequestParam Long addressId) throws NoSuchAlgorithmException {
            OrderDto orders = iOrderService.placeOrder(userId,addressId);
            return ResponseEntity.status(HttpStatus.CREATED).body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
            OrderDto order =  iOrderService.getOrder(orderId);
            return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable Long userId) {
            List<OrderDto> orders = iOrderService.getUserOrder(userId);
            return ResponseEntity.ok(orders);
    }
}
