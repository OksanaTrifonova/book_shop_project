package com.oksanatrifonova.bookshop.repository;

import com.oksanatrifonova.bookshop.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Image, Long> {
}
