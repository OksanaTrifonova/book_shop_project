package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.entity.Images;
import com.oksanatrifonova.bookshop.repository.ImagesRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@AllArgsConstructor
public class ImageService {
    private ImagesRepository imagesRepository;

    public ResponseEntity<?> getImageById(Long id) {
        Images image = imagesRepository.findById(id).orElseThrow(null);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }
}
