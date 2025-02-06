package com.Myproject.ShoppingCart.Service.UserInfo;

import com.Myproject.ShoppingCart.Models.Address;
import com.Myproject.ShoppingCart.Models.User;
import com.Myproject.ShoppingCart.Repository.AddressRepository;
import com.Myproject.ShoppingCart.Request.createAddressRequest;
import com.Myproject.ShoppingCart.Validation.UserValidator;
import com.Myproject.ShoppingCart.dto.AddressDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInfoImp implements IUserInfo{
    private final AddressRepository addressRepository;
    private final UserValidator userValidator;
    private final ModelMapper mapper;

    @Override
    public createAddressRequest createAddress(Long userId, createAddressRequest request) {
        User user = userValidator.validateUserNotFound(userId);
        Address address = setupAddress(user,request);
        addressRepository.save(address);
        return request;
    }

    private Address setupAddress(User user, createAddressRequest request) {
        return Address.builder()
                .user(user)
                .district(request.getDistrict())
                .street(request.getStreet())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .country(request.getCountry() != null ? request.getCountry() : "Cambodia")
                .build();
    }

    @Override
    public List<AddressDto> getAllAddress(Long userId){
        List<Address> address = addressRepository.findAllAddressByUserId(userId);
        return convertToListOfAddressDtos(address);
    }

    @Override
    public void deleteAddress(Long id){
        addressRepository.deleteById(id);
    }

    @Override
    public AddressDto convertToDto(Address address){
        return mapper.map(address,AddressDto.class);
    }

    @Override
    public List<AddressDto> convertToListOfAddressDtos(List<Address> addresses){
        return addresses.stream().map(this::convertToDto).toList();
    }
}
