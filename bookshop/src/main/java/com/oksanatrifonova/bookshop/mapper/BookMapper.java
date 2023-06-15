package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.entity.Author;
import com.oksanatrifonova.bookshop.entity.Image;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Component
@AllArgsConstructor
public class BookMapper {


    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPrice(book.getPrice());
        bookDto.setCategory(book.getCategory());
        bookDto.setDescription(book.getDescription());
        bookDto.setImageId(book.getImages() != null ? book.getImages().getId() : null);

        List<Long> authorIds = new ArrayList<>();
        List<String> authorNames = new ArrayList<>();
        for (Author author : book.getAuthors()) {
            authorIds.add(author.getId());
            authorNames.add(author.getName());
        }
        bookDto.setAuthorIds(authorIds);
        bookDto.setAuthorNames(authorNames);
        bookDto.setActive(book.isActive());
        return bookDto;
    }

    public Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setPrice(bookDto.getPrice());
        book.setCategory(bookDto.getCategory());
        book.setDescription(bookDto.getDescription());

        List<Author> authors = new ArrayList<>();
        for (Long authorId : bookDto.getAuthorIds()) {
            Author author = new Author();
            author.setId(authorId);
            authors.add(author);
        }
        book.setAuthors(new HashSet<>(authors));
        book.setActive(bookDto.isActive());
        Image image = new Image();
        image.setId(bookDto.getImageId());
        book.setImages(image);

        return book;
    }

}