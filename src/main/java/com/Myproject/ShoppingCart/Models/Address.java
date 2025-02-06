package com.Myproject.ShoppingCart.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Address
    @NotNull
    @Column(nullable = true)
    private String street;

    @NotNull
    @Size(min = 3, max = 50)
    private String district;

    @NotNull
    @Size(min = 3, max = 50)
    private String province;

    @Column(nullable = true)
    private String postalCode;

    @Column(nullable = true)
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
