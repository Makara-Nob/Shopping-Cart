package com.Myproject.ShoppingCart.Repository;

import com.Myproject.ShoppingCart.Models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllAddressByUserId(Long id);
}
