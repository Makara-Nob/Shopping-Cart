package com.Myproject.ShoppingCart.Service.Cart;

import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Models.CartItem;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Repository.CartItemRepository;
import com.Myproject.ShoppingCart.Repository.CartRepository;
import com.Myproject.ShoppingCart.Service.product.IProductService;
import com.Myproject.ShoppingCart.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService iProductService;
    private final CartService cartService;
    private final ModelMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(CartItemService.class);

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. get the cart
        //2. get product
        //3. check if product is already exist
        //4. if YES increase the qty with the requested qty
        //5. if NO then initiate a new cartItem entry
        logger.info("Executing: " + getClass() + ", Input: " + cartId + ", " + productId + ", " + quantity);
        Cart cart = cartService.getCart(cartId);
        ProductDto product = iProductService.getProductById(productId);

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst().orElse(new CartItem());
        if(cartItem.getId() == null)
        {
            cartItem.setCart(cart);
            cartItem.setProduct(mapper.map(product,Product.class));
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
            Cart cart = cartService.getCart(cartId);
            CartItem itemToRemove = getCartItem(cartId,productId);
            cart.removeItem(itemToRemove);
            cartItemRepository.delete(itemToRemove);
            cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
                Cart cart = cartService.getCart(cartId);
                cart.getItems()
                        .stream()
                        .filter(item -> item.getProduct().getId() == productId)
                        .findFirst()
                        .ifPresent(item -> {
                            item.setQuantity(quantity);
                            item.setUnitPrice(item.getProduct().getPrice());
                            item.setTotalPrice();
                        });
        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem :: getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("Item not found!"));
    }


}
