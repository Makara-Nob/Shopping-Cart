package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Service.Image.ImageService;
import com.Myproject.ShoppingCart.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> saveImage(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
            List<ImageDto> imageDto = imageService.saveImage(files,productId);
            return ResponseEntity
                    .ok(imageDto);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<String> getImageUrl(@PathVariable Long imageId) throws IOException, SQLException {
        String imageUrl = imageService.fetchImageUrl(imageId);
        return ResponseEntity.ok(imageUrl);
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<?> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) throws IOException {
                imageService.deleteImage(imageId);
                return ResponseEntity
                        .noContent().build();
    }


}
