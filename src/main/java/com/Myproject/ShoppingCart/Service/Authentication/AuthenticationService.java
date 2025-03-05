package com.Myproject.ShoppingCart.Service.Authentication;

import com.Myproject.ShoppingCart.Request.CreateUserRequest;
import com.Myproject.ShoppingCart.Request.LoginRequest;
import com.Myproject.ShoppingCart.Response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse login(LoginRequest request, HttpServletResponse response);

    AuthenticationResponse register(CreateUserRequest request, HttpServletResponse response);
    void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
