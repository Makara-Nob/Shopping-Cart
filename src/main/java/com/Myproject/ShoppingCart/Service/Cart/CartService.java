package com.Myproject.ShoppingCart.Service.Cart;

import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Repository.CartItemRepository;
import com.Myproject.ShoppingCart.Repository.CartRepository;
import com.Myproject.ShoppingCart.dto.CartDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(CartService.class);
    @Override
    public Cart getCart(Long id) {
        logger.info("Executing: " + getClass() + ", Input: " + id);

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFOundException("Cart not found!"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()-> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public CartDto convertToDto(Cart cart) {
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        System.out.println(cartDto);
        return cartDto;
    }

}
