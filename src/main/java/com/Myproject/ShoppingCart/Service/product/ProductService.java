package com.Myproject.ShoppingCart.Service.product;

import com.Myproject.ShoppingCart.Exception.ProductNotFoundException;
import com.Myproject.ShoppingCart.Filter.ProductFilter;
import com.Myproject.ShoppingCart.Filter.ProductSpec;
import com.Myproject.ShoppingCart.Models.Category;
import com.Myproject.ShoppingCart.Models.Image;
import com.Myproject.ShoppingCart.Models.Product;
import com.Myproject.ShoppingCart.Pagination.ProductResponse;
import com.Myproject.ShoppingCart.Repository.CategoryRepository;
import com.Myproject.ShoppingCart.Repository.ImageRepository;
import com.Myproject.ShoppingCart.Repository.ProductRepository;
import com.Myproject.ShoppingCart.Request.AddProductRequest;
import com.Myproject.ShoppingCart.Request.ProductUpdateRequest;
import com.Myproject.ShoppingCart.Validation.ProductValidator;
import com.Myproject.ShoppingCart.dto.ImageDto;
import com.Myproject.ShoppingCart.dto.PageDto;
import com.Myproject.ShoppingCart.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService{
    private final ProductValidator validator;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        validator.ValidateProductAlreadyExist(request.getBrand(),request.getName());
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
        });
        request.setCategory(category);
        return productRepository.save(createProduct(request,category));
    }

    @Override
    public ProductResponse getAllProduct(Map<String,String> params) {

        ProductFilter productFilter = ProductFilter.buildProductFilter(params);
        Pageable pageable = PageDto.buildPageable(params);

        ProductSpec productSpec = new ProductSpec(productFilter);
        //Retrieve a page of products
        Page<Product> productPage = productRepository.findAll(productSpec, pageable);
        //convert it to Dto 
        List<ProductDto> content = productPage.stream()
                .map(product -> modelMapper.map(product,ProductDto.class))
                .collect(Collectors.toList());

        return getProductResponse(content, productPage);
    }

    private static ProductResponse getProductResponse(List<ProductDto> content, Page<Product> products) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setLast(products.isLast());
        return productResponse;
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
    public ProductDto getProductById(Long id) {
         Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
         return convertToDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductUpdateRequest request, Long productId) {
            Product product = productRepository.findById(productId)
                    .map(existingProduct -> updateExistingProduct(existingProduct,request))
                    .map(productRepository::save)
                    .orElseThrow(()->new ProductNotFoundException("Product not found!"));
        return convertToDto(product);
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
        productRepository.deleteById(id);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
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
