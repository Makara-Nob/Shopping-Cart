package com.Myproject.ShoppingCart.Service.UserInfo;

import com.Myproject.ShoppingCart.Models.Address;
import com.Myproject.ShoppingCart.Request.createAddressRequest;
import com.Myproject.ShoppingCart.dto.AddressDto;

import java.util.List;

public interface IUserInfo {
    createAddressRequest createAddress(Long userId,createAddressRequest request);
    List<AddressDto> getAllAddress(Long userId);

    void deleteAddress(Long id);

    AddressDto convertToDto(Address address);

    List<AddressDto> convertToListOfAddressDtos(List<Address> addresses);
}
