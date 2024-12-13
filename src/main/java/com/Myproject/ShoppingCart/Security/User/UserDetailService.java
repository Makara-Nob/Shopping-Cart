package com.Myproject.ShoppingCart.Security.User;

import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Repository.UserRepository;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        return UserDetail.buildUserDto(user);
    };

}
