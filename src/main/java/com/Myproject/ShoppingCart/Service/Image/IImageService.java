package com.Myproject.ShoppingCart.Service.Image;

import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImage(Long id) throws IOException;
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId) throws Exception;
    void updateImage(MultipartFile file, Long imageId);
    String fetchImageUrl(Long id);
}
