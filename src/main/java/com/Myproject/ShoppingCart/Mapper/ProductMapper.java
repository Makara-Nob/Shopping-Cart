package com.Myproject.ShoppingCart.Mapper;

import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring",uses = ImageMapper.class)
public interface ProductMapper {

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "images",target = "images")
    ProductDto productToDto(Product product);
    List<ProductDto> productListToDtoList(List<Product> products);

    @Mapping(source = "productId", target = "id")
    @Mapping(source = "images",target = "images")
    Product productDtoToEntity(ProductDto product);
}
