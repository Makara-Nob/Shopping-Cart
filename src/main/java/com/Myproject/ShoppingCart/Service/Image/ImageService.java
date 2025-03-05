package com.Myproject.ShoppingCart.Service.Image;

import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Repository.ImageRepository;
import com.Myproject.ShoppingCart.Service.product.IProductService;
import com.Myproject.ShoppingCart.dto.ImageDto;
import com.Myproject.ShoppingCart.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IProductService iProductService;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Override
    public Image getImageById(Long id) {
        logger.info("Executing: " + getClass() + ", Input: " + id);
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found!"));
    }

    @Override
    public void deleteImage(Long id) throws IOException {
            Image image = getImageById(id);
            cloudinaryService.deleteImage(image.getPublicId());
            imageRepository.delete(image);
    }

    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        logger.info("Executing: " + getClass() + ", Input: " + files + ", " + productId);;
        ProductDto product = iProductService.getProductById(productId); // Ensure product exists
        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Map<String,String> uploadResult = cloudinaryService.uploadImage(file);
                String imageUrl = uploadResult.get("secure_url");
                String publicId = uploadResult.get("public_id");

                Image image = Image.builder()
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .downloadUrl(imageUrl)
                        .publicId(publicId)
                        .product(mapper.map(product,Product.class))
                        .build();

                Image savedImage = imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                imageDto.setPublicId(savedImage.getPublicId());
                savedImageDto.add(imageDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            if(image != null ){
                cloudinaryService.deleteImage(image.getPublicId());
                Map<String,String> imageResult = cloudinaryService.uploadImage(file);
                String imageUrl = imageResult.get("secure_url");
                image.setFileName(file.getOriginalFilename());
                image.setDownloadUrl(imageUrl);
                imageRepository.save(image);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage(), e);
        }
    }

    @Override
    public String fetchImageUrl(Long id) {
        Image image = getImageById(id);
        return image.getDownloadUrl();
    }
}
