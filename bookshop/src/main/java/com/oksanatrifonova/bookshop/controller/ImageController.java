package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class ImageController {
    private ImageService imageService;

    @GetMapping("/images/{id}")
    public ResponseEntity<InputStreamResource> getImageById(@PathVariable Long id) {
        return imageService.getImageById(id);
    }
}
