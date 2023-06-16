package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.AuthorDto;
import com.oksanatrifonova.bookshop.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBirthYear(formatYear(author.getBirthYear()));
        dto.setDeathYear(formatYear(author.getDeathYear()));
        dto.setActive(author.isActive());
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
