package com.Myproject.ShoppingCart.Service.product;

import com.Myproject.ShoppingCart.Exception.AlreadyExistsException;
import com.Myproject.ShoppingCart.Exception.ProductNotFoundException;
import com.Myproject.ShoppingCart.Exception.ResourceNotFOundException;
import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Repository.CategoryRepository;
import com.Myproject.ShoppingCart.Repository.ImageRepository;
import com.Myproject.ShoppingCart.Repository.ProductRepository;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.dto.ImageDto;
import com.Myproject.ShoppingCart.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {

        if (existingProduct(request.getName(), request.getBrand()))
        {
            throw new AlreadyExistsException("Product name: " + request.getName() + ", Brand: " + request.getBrand() + " already exist.");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
        });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    private boolean existingProduct(String name, String brand) {
        return productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
            return new Product(
                    request.getName(),
                    request.getBrand(),
                    request.getPrice(),
                    request.getInventory(),
                    request.getDescription(),
                    category
            );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFOundException("Product not found!"));
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
            return productRepository.findById(productId)
                    .map(existingProduct -> updateExistingProduct(existingProduct,request))
                    .map(productRepository::save)
                    .orElseThrow(()->new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                ()-> {throw new ProductNotFoundException("Product not found!");});
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }

    @Override
    public List<ProductDto> convertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }
    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product,ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
