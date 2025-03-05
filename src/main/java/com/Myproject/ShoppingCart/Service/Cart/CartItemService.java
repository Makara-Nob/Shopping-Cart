package com.Myproject.ShoppingCart.Service.Cart;

import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
import com.Myproject.ShoppingCart.Models.Cart;
import com.Myproject.ShoppingCart.Models.CartItem;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Repository.CartItemRepository;
import com.Myproject.ShoppingCart.Repository.CartRepository;
import com.Myproject.ShoppingCart.Service.product.IProductService;
import com.Myproject.ShoppingCart.dto.ProductDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
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

    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Get the cart
        // 2. Get the product
        // 3. Check if the product already exists in the cart
        // 4. If YES, increase the quantity
        // 5. If NO, create a new CartItem entry
        logger.info("Executing: {}, Input: {}, {}, {}", getClass(), cartId, productId, quantity);

        // Get cart and product
        Cart cart = cartService.getCart(cartId);
        ProductDto productDto = iProductService.getProductById(productId);

        // Find existing CartItem in the cart
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setProduct(mapper.map(productDto, Product.class));
                    return newCartItem;
                });

        // Update quantity and price
        if (cartItem.getId() == null) {
            // New item
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(productDto.getPrice());
        } else {
            // Existing item, update quantity
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        // Calculate the total price for the cart item
        cartItem.setTotalPrice();

        // Add or update cart item
        cart.addItem(cartItem);

        // Save the updated cart item and cart
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        logger.info("Executing: {}, Input: {}, {}", getClass(), cartId, productId);

        // Get the cart and the item to remove
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);

        // Remove the item from the cart
        cart.removeItem(itemToRemove);

        // Delete the item and save the updated cart
        cartItemRepository.delete(itemToRemove);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) throws BadRequestException {

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        // Get the cart
        Cart cart = cartService.getCart(cartId);

        // Find the item in the cart
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .ifPresent(item -> {
                    // Update quantity and unit price
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        // Recalculate total price for the cart
        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Update the total amount of the cart
        cart.setTotalAmount(totalAmount);

        // Save the updated cart
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
