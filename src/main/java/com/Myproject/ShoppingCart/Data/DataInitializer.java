package com.Myproject.ShoppingCart.Data;

import com.Myproject.ShoppingCart.Models.Role;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Repository.RoleRepository;
import com.Myproject.ShoppingCart.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
            Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExist();
        createDefaultRoleIfNotExist(defaultRoles);
        createDefaultAdminIfNotExist();
    }

    private void createDefaultRoleIfNotExist(Set<String> roles) {
        roles.forEach(role -> {
            if (roleRepository.findByName(role).isEmpty()) {
                Role newRole = new Role(role);
                roleRepository.save(newRole);
                System.out.println("Created role: " + role);
            } else {
                System.out.println("Role already exists: " + role);
            }
        });
    }


    private void createDefaultUserIfNotExist() {
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i=1; i <= 5; i++) {
            String defaultEmail = "user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirst_name("The user");
            user.setLast_name("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("Secret#12@"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default user " + i + " create successfully");
        }
    }

    private void createDefaultAdminIfNotExist() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for(int i=1; i <= 5; i++) {
            String defaultEmail = "admin"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirst_name("Admin");
            user.setLast_name("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("Secret#12@"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " create successfully");
        }
    }
}
