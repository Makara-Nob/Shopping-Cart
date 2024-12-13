package com.Myproject.ShoppingCart.Repository;

import com.Myproject.ShoppingCart.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
