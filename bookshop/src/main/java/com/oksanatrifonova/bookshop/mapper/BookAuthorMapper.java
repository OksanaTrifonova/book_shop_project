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
        dto.setBirthYear(bookAuthor.getBirthYear());
        dto.setDeathYear(bookAuthor.getDeathYear());
        dto.setActive(bookAuthor.isActive());
        return dto;
    }

}
