package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Request.createAddressRequest;
import com.Myproject.ShoppingCart.Service.UserInfo.IUserInfo;
import com.Myproject.ShoppingCart.dto.AddressDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserInfoController {
    private final IUserInfo userService;

    @PostMapping("/info/{userId}")
    public ResponseEntity<?> createAddress(@PathVariable Long userId, @Valid @RequestBody createAddressRequest request) {
        createAddressRequest address = userService.createAddress(userId,request);
        return new ResponseEntity<>(address,HttpStatus.CREATED);
    }

    @GetMapping("/info/{userId}")
    public ResponseEntity<?> getUserAddress(@PathVariable Long userId){
        List<AddressDto> address = userService.getAllAddress(userId);
        return ResponseEntity.ok(address);
    }
}
