package com.Myproject.ShoppingCart.Service.User;

import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Request.CreateUserRequest;
import com.Myproject.ShoppingCart.Request.UpdateUserRequest;
import com.Myproject.ShoppingCart.dto.UserDto;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);
    UserDto convertToUserDto(User user);
    User getAuthenticatedUser();
}
