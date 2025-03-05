package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Request.CreateUserRequest;
import com.Myproject.ShoppingCart.Request.LoginRequest;
import com.Myproject.ShoppingCart.Response.AuthenticationResponse;
import com.Myproject.ShoppingCart.Service.Authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
            return ResponseEntity.ok(authenticationService.login(request, response));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody CreateUserRequest request, HttpServletResponse response) {
            return ResponseEntity.ok(authenticationService.register(request, response));
    }

    @PostMapping("/refresh-token")
    public void refreshAccessToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
            authenticationService.refreshAccessToken(request,response);
    }
}
