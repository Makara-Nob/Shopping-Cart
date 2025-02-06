package com.Myproject.ShoppingCart.Validation;

import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public User validateUserNotFound(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User with id '" + userId + "' not found."));
    }
}
