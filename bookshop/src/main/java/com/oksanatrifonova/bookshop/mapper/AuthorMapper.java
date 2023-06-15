package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.AuthorDto;
import com.oksanatrifonova.bookshop.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author bookAuthor) {
        AuthorDto dto = new AuthorDto();
        dto.setId(bookAuthor.getId());
        dto.setName(bookAuthor.getName());
        dto.setBirthYear(formatYear(bookAuthor.getBirthYear()));
        dto.setDeathYear(formatYear(bookAuthor.getDeathYear()));
        dto.setActive(bookAuthor.isActive());
        return dto;
    }
    private String formatYear(Integer year) {
        if (year == null) {
            return null;
        } else if (year < 0) {
            return Math.abs(year) + " BC";
        } else {
            return year.toString();
        }
    }
}
