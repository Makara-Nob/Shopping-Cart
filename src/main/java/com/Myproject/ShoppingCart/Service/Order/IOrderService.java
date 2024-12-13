package com.Myproject.ShoppingCart.Service.Order;

import com.Myproject.ShoppingCart.Models.Order;
import com.Myproject.ShoppingCart.dto.OrderDto;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrder(Long userId);

    OrderDto convertToDto(Order order);
}
