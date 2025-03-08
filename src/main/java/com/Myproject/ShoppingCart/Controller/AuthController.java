package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Request.CreateUserRequest;
import com.Myproject.ShoppingCart.Request.LoginRequest;
import com.Myproject.ShoppingCart.Response.ApiResponse;
import com.Myproject.ShoppingCart.Response.AuthenticationResponse;
import com.Myproject.ShoppingCart.Service.Authentication.AuthenticationService;
import com.Myproject.ShoppingCart.Service.Cart.ICartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    /**
     * Authenticate a user and return a JWT token.
     * @param request user login credentials
     * @param response HTTP response for setting cookies if needed
     * @return an API response containing authentication details
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse = authenticationService.login(request, response);
        ApiResponse apiResponse = new ApiResponse("success", "Login successful", authResponse);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Register a new user and return authentication details.
     * @param request user registration details
     * @param response HTTP response for setting cookies if needed
     * @return an API response containing authentication details
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody CreateUserRequest request, HttpServletResponse response) {
        AuthenticationResponse authResponse = authenticationService.register(request, response);
        ApiResponse apiResponse = new ApiResponse("success", "User registered successfully", authResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    /**
     * Refresh the access token using a refresh token.
     * @param request HTTP request containing refresh token
     * @param response HTTP response to send back the new token
     * @throws IOException if an issue occurs during token refresh
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshAccessToken(request, response);
        ApiResponse apiResponse = new ApiResponse("success", "Token refreshed successfully", null);
        return ResponseEntity.ok(apiResponse);
    }
}
