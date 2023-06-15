package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.entity.Image;
import com.oksanatrifonova.bookshop.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@AllArgsConstructor
public class ImageService {
    private ImageRepository imagesRepository;

    public ResponseEntity<InputStreamResource> getImageById(Long id) {
        Image image = imagesRepository.findById(id).orElseThrow(null);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
