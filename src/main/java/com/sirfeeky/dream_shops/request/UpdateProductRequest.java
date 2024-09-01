package com.sirfeeky.dream_shops.request;

import java.math.BigDecimal;

import com.sirfeeky.dream_shops.model.Category;

import lombok.Data;

@Data
public class UpdateProductRequest {
   private String name;

    private BigDecimal price;

    private int inventory;

    private String brand;

    private String description;

    private Category category;
}
