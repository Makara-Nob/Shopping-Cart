package com.Myproject.ShoppingCart.Service.Image;

import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Repository.ImageRepository;
import com.Myproject.ShoppingCart.Service.product.IProductService;
import com.Myproject.ShoppingCart.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService iProductService;
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Override
    public Image getImageById(Long id) {
        logger.info("Executing: " + getClass() + ", Input: " + id);
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFOundException("Image not found!"));
    }

    @Override
    public void deleteImage(Long id) {
            imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, ()-> {
                throw new ResourceNotFOundException("No image found with id " + id);
            });
    }

    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        logger.info("Executing: " + getClass() + ", Input: " + files + ", " + productId);;
        Product product = iProductService.getProductById(productId); // Ensure product exists
        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";

                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage(), e);
        }
    }



}
