package com.sirfeeky.dream_shops.service.category;

import java.util.List;

import com.sirfeeky.dream_shops.model.Category;

public interface ICategoryService {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategoryById(Long id);
    
}
