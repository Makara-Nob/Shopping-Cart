package com.Myproject.ShoppingCart.Controller;

import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.Service.Image.ImageService;
import com.Myproject.ShoppingCart.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> saveImage(@RequestParam List<MultipartFile> files, @RequestParam Long Id) {
            List<ImageDto> imageDto = imageService.saveImage(files,Id);
            return ResponseEntity
                    .ok(imageDto);
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws IOException, SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "Attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<?> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long imageId) {
                imageService.deleteImage(imageId);
                return ResponseEntity
                        .noContent().build();
    }


}
