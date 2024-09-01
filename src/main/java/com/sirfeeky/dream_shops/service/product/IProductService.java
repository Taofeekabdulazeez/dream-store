package com.sirfeeky.dream_shops.service.product;

import java.util.List;

import com.sirfeeky.dream_shops.model.Product;
import com.sirfeeky.dream_shops.request.AddProductRequest;
import com.sirfeeky.dream_shops.request.UpdateProductRequest;

public interface IProductService {

    Product addProduct(AddProductRequest product);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(UpdateProductRequest request, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductByBrandAndName(String category, String name);

    Long countProductsByBrandAndName(String brand, String name);

} 