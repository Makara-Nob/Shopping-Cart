package com.Myproject.ShoppingCart.Service.Order;

import com.Myproject.ShoppingCart.Models.Order;
import com.Myproject.ShoppingCart.dto.OrderDto;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IOrderService {
    OrderDto placeOrder(Long userId, Long addressId) throws NoSuchAlgorithmException;
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrder(Long userId);
    OrderDto convertToDto(Order order);
}
