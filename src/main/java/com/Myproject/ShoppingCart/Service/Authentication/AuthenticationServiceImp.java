package com.Myproject.ShoppingCart.Service.Authentication;

import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Repository.UserRepository;
import com.Myproject.ShoppingCart.Request.CreateUserRequest;
import com.Myproject.ShoppingCart.Request.LoginRequest;
import com.Myproject.ShoppingCart.Response.AuthenticationResponse;
import com.Myproject.ShoppingCart.Security.JWT.JwtUtils;
import com.Myproject.ShoppingCart.Security.User.UserDetail;
import com.Myproject.ShoppingCart.Security.User.UserDetailService;
import com.Myproject.ShoppingCart.Service.Cart.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailService userDetailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;

    @Override
    public AuthenticationResponse login(LoginRequest request,HttpServletResponse response) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(userDetail);
        String refreshToken = jwtUtils.generateRefreshToken(userDetail);
        storeRefreshTokenInHttpOnlyCookie(response,refreshToken);
        return AuthenticationResponse.builder()
                .id(userDetail.getId())
                .name(userDetail.getName())
                .access_token(accessToken)
                .build();
    }

    @Override
    public AuthenticationResponse register(CreateUserRequest request, HttpServletResponse response) {
        // Check if the email already exists, and throw an exception if it does.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceNotFoundException(request.getEmail() + " already exists!");
        }

        // Create and save the new user if the email does not exist.
        User user = new User();
        user.setFirst_name(request.getFirst_name());
        user.setLast_name(request.getLast_name());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        cartService.initializeNewCart(user);

        // Authenticate the user immediately after registration
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(userDetail);
        String refreshToken = jwtUtils.generateRefreshToken(userDetail);
        storeRefreshTokenInHttpOnlyCookie(response,refreshToken);
        return AuthenticationResponse.builder()
                .id(userDetail.getId())
                .name(userDetail.getName())
                .access_token(accessToken)
                .build();
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (!StringUtils.hasText(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Refresh token is missing\"}");
            return;
        }

        String email;

        try {
            email = jwtUtils.getUsernameFromToken(refreshToken);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired refresh token\"}");
            return;
        }

        if (email == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired refresh token\"}");
            return;
        }

        UserDetail userDetail;
        try {
            userDetail = userDetailService.loadUserByUsername(email);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"User not found\"}");
            return;
        }

        // Validate the refresh token
        if (!jwtUtils.validateToken(refreshToken, userDetail)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired refresh token\"}");
            return;
        }

        // Generate a new access token
        String accessToken = jwtUtils.generateAccessToken(userDetail);

        storeRefreshTokenInHttpOnlyCookie(response,refreshToken);

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .id(userDetail.getId())
                .name(userDetail.getName())
                .access_token(accessToken)
                .build();

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }

    private void storeRefreshTokenInHttpOnlyCookie(HttpServletResponse response, String refreshToken) {
        Cookie httpOnlyCookie = new Cookie("refresh_token", refreshToken);
        httpOnlyCookie.setHttpOnly(true);
        httpOnlyCookie.setSecure(false); // Set to true in production (requires HTTPS)
        httpOnlyCookie.setPath("/"); // Cookie is accessible across the entire site
        httpOnlyCookie.setMaxAge(7 * 24 * 60 * 60); // Set expiration to 7 days

        response.addCookie(httpOnlyCookie);
    }

}
