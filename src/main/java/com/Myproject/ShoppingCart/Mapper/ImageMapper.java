package com.Myproject.ShoppingCart.Mapper;

import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.dto.ImageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(source = "fileName",target = "imageName")
    ImageDto imageToDto(Image image);
    List<ImageDto> imageListToListDto(List<Image> images);
}
