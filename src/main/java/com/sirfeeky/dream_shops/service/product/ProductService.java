package com.sirfeeky.dream_shops.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.sirfeeky.dream_shops.dto.ImageDto;
import com.sirfeeky.dream_shops.dto.ProductDto;
import com.sirfeeky.dream_shops.exceptions.ResourceNotFoundException;
import com.sirfeeky.dream_shops.model.Category;
import com.sirfeeky.dream_shops.model.Image;
import com.sirfeeky.dream_shops.model.Product;
import com.sirfeeky.dream_shops.repository.CategoryRepository;
import com.sirfeeky.dream_shops.repository.ImageRepository;
import com.sirfeeky.dream_shops.repository.ProductRepository;
import com.sirfeeky.dream_shops.request.AddProductRequest;
import com.sirfeeky.dream_shops.request.UpdateProductRequest;
// import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // used for constructor injection (NB: Injected property must be final)
public class ProductService implements IProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;


    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category is found in the database
        // if yes, set it as the new product category
        // if no, then save it as a new category
        // then set it as the new product category

        String categoryName = request.getCategory().getName();

        Category category = Optional.ofNullable(this.categoryRepository.findByName(categoryName))
                               .orElseGet(() -> {
                                 Category newCategory = new Category(categoryName);

                                 return this.categoryRepository.save(newCategory);
                });

        request.setCategory(category);

        return this.productRepository.save(createProduct(request, category));
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
        return this.productRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
       this.productRepository.findById(id).ifPresentOrElse(this.productRepository::delete, 
               () -> {
                   throw new ResourceNotFoundException("Product not found");
                });
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
       return this.productRepository.findById(productId)
           .map(existingProduct -> updateExistingProduct(existingProduct, request))
           .map(productRepository :: save)
               .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
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
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return this.productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return this.productRepository.findByBrandName(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return this.productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return this.productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return this.productRepository.countByBrandAndName(brand, name);
    }

     @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
      return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
    
}
