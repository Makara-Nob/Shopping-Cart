package com.Myproject.ShoppingCart.Service.product;

import com.Myproject.ShoppingCart.Exception.ProductNotFoundException;
import com.Myproject.ShoppingCart.Exception.ResourceNotFoundException;
import com.Myproject.ShoppingCart.Filter.ProductFilter;
import com.Myproject.ShoppingCart.Filter.ProductSpec;
import com.Myproject.ShoppingCart.Mapper.ProductMapper;
import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Pagination.ProductResponse;
import com.Myproject.ShoppingCart.Repository.CategoryRepository;
import com.Myproject.ShoppingCart.Repository.ImageRepository;
import com.Myproject.ShoppingCart.Repository.ProductRepository;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.Service.Image.CloudinaryService;
import com.Myproject.ShoppingCart.Validation.ProductValidator;
import com.Myproject.ShoppingCart.dto.PageDto;
import com.Myproject.ShoppingCart.dto.ProductDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {

    private final ProductValidator validator;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    @Transactional
    @Override
    public ProductDto addProduct(String clientRequest, List<MultipartFile> files) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AddProductRequest request = mapper.readValue(clientRequest, AddProductRequest.class);

        validator.ValidateProductAlreadyExist(request.getBrand(), request.getName());

        Category category = findOrCreateCategory(request.getCategoryId(), request.getCategoryName());

        Product savedProduct = productRepository.save(createProduct(request, category, files));

        log.info("Product '{}' added successfully.", savedProduct.getName());
        return productMapper.productToDto(savedProduct);
    }

    @Override
    public ProductResponse getAllProduct(Map<String, String> params) {
        Pageable pageable = PageDto.buildPageable(params);
        ProductSpec productSpec = new ProductSpec(ProductFilter.buildProductFilter(params));

        Page<Product> productPage = productRepository.findAll(productSpec, pageable);
        List<ProductDto> content = productMapper.productListToDtoList(productPage.getContent());

        return getProductResponse(content, productPage);
    }

    private static ProductResponse getProductResponse(List<ProductDto> content, Page<Product> page) {
        return ProductResponse.builder()
                .content(content)
                .pageNo(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .last(page.isLast())
                .build();
    }

    private Product createProduct(AddProductRequest request, Category category, List<MultipartFile> files) throws IOException {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .inventory(request.getInventory())
                .brand(request.getBrand())
                .category(category)
                .description(request.getDescription())
                .build();

        productRepository.save(product);
        saveImages(files, product);

        return product;
    }

    private void saveImages(List<MultipartFile> files, Product product) {
        List<Image> images = files.stream().map(file -> {
            try {
                Map<String, String> uploadResult = cloudinaryService.uploadImage(file);
                return Image.builder()
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .downloadUrl(uploadResult.get("secure_url"))
                        .publicId(uploadResult.get("public_id"))
                        .product(product)
                        .build();
            } catch (IOException e) {
                log.error("Image upload failed for file: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Failed to upload image", e);
            }
        }).collect(Collectors.toList());

        imageRepository.saveAll(images);
    }

    private Category findOrCreateCategory(Long categoryId, String categoryName) {
        return categoryRepository.findById(categoryId)
                .orElseGet(() -> {
                    log.info("Category '{}' not found. Creating a new category.", categoryId);
                    return categoryRepository.save(new Category(categoryName));
                });
    }

    @Override
    public ProductDto getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found!", id);
                    return new ProductNotFoundException("Product not found!");
                });

        return productMapper.productToDto(product);
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductUpdateRequest request, Long productId) {
        log.info("Updating product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        log.info("Product with ID {} updated successfully.", productId);
        return productMapper.productToDto(product);
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = findOrCreateCategory(request.getCategory().getId(),request.getName());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

        // Delete images from Cloudinary
        product.getImages().forEach(image -> {
            try {
                cloudinaryService.deleteImage(image.getPublicId());
                log.info("Deleted image: {}", image.getPublicId());
            } catch (Exception e) {
                log.warn("Failed to delete image: {}", image.getPublicId());
            }
        });

        imageRepository.deleteAll(product.getImages());
        productRepository.delete(product);

        log.info("Product with ID {} deleted successfully.", id);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
