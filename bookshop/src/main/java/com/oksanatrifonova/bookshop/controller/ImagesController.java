package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class ImagesController {
    private ImageService imagesService;

    @GetMapping("/images/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        return imagesService.getImageById(id);
    }
}