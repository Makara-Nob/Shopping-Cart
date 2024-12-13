package com.Myproject.ShoppingCart.Service.User;

import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Repository.UserRepository;
import com.Myproject.ShoppingCart.Request.CreateUserRequest;
import com.Myproject.ShoppingCart.Request.UpdateUserRequest;
import com.Myproject.ShoppingCart.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFOundException("User not found."));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirst_name(request.getFirst_name());
                    user.setLast_name(request.getLast_name());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return userRepository.save(user);
                }).orElseThrow(()->new ResourceNotFOundException(request.getEmail() + " already exist!"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirst_name(request.getFirst_name());
            existingUser.setLast_name(request.getLast_name());
            return userRepository.save(existingUser);
        }).orElseThrow(()-> new ResourceNotFOundException("User not found."));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, ()->{
            throw new ResourceNotFOundException("User not found");
        });
    }

    @Override
    public UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
