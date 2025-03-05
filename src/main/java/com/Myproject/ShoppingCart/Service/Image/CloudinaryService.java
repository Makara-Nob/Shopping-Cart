package com.Myproject.ShoppingCart.Service.Image;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret)
    {
        this.cloudinary = new Cloudinary(Cloudinary.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public Map<String,String>uploadImage(MultipartFile file) throws IOException {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),Cloudinary.asMap(
                    "folder", "e-commerce"
            ));
            return Map.of(
                    "secure_url",uploadResult.get("secure_url").toString(),
                    "public_id",uploadResult.get("public_id").toString()
            );
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image to Cloudinary",e);
        }
    }

    public void deleteImage(String publicId) throws IOException {
        try {
            cloudinary.uploader().destroy(publicId,Cloudinary.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary",e);
        }
    }
}
