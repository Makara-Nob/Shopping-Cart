package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Request.UpdateUserRequest;
import com.Myproject.ShoppingCart.Security.User.UserDetail;
import com.Myproject.ShoppingCart.Service.User.IUserService;
import com.Myproject.ShoppingCart.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertToUserDto(user);
            return ResponseEntity.ok(userDto);
    }

    @GetMapping("/me")
    public UserDetail getUser(@AuthenticationPrincipal UserDetail userDetail) {
            return userDetail;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId) {
            User user = userService.updateUser(request,userId);
            UserDto userDto = userService.convertToUserDto(user);
            return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
    }
}