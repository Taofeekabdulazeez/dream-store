package com.sirfeeky.dream_shops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sirfeeky.dream_shops.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
