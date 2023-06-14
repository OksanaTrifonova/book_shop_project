package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.entity.BookAuthor;
import org.springframework.stereotype.Component;

@Component
public class BookAuthorMapper {

    public BookAuthorDto toDto(BookAuthor bookAuthor) {
        BookAuthorDto dto = new BookAuthorDto();
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
