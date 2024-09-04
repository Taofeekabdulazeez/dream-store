package com.sirfeeky.dream_shops.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sirfeeky.dream_shops.dto.ImageDto;
import com.sirfeeky.dream_shops.exceptions.ResourceNotFoundException;
import com.sirfeeky.dream_shops.model.Image;
import com.sirfeeky.dream_shops.model.Product;
import com.sirfeeky.dream_shops.repository.ImageRepository;
import com.sirfeeky.dream_shops.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new ResourceNotFoundException("Image not found");
           });
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String downloadUrl = "/api/v1/images/image/download/" + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl("/api/v1/images/image/download/" + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();

                imageDto.setImageName(savedImage.getFileName());
                imageDto.setImageId(savedImage.getId());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                savedImageDto.add(imageDto);

            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());

            }
        }
        
        return savedImageDto;
        
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        try {
            Image image = this.getImageById(imageId);
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

        } catch (IOException | SQLException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
    
}
