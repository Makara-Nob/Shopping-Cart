package com.Myproject.ShoppingCart.Response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"status", "message", "data"})
public class ApiResponse {
    private String status;
    private String message;
    private Object data;
}
