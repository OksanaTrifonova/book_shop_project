package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.entity.BookAuthor;
import com.oksanatrifonova.bookshop.entity.Images;
import com.oksanatrifonova.bookshop.repository.BookAuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Component
@AllArgsConstructor
public class BookMapper {
    private BookAuthorRepository bookAuthorRepository;
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
        for (BookAuthor author : book.getAuthors()) {
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

        List<BookAuthor> authors = new ArrayList<>();
        for (Long authorId : bookDto.getAuthorIds()) {
            bookAuthorRepository.findById(authorId).ifPresent(authors::add);
        }
        book.setAuthors(new HashSet<>(authors));

        book.setActive(bookDto.isActive());

        Images image = new Images();
        image.setId(bookDto.getImageId());
        book.setImages(image);

        return book;
    }

    public Set<BookAuthor> mapAuthorIdsToAuthors(List<Long> authorIds) {
        Set<BookAuthor> authors = new HashSet<>();
        for (Long authorId : authorIds) {
            bookAuthorRepository.findById(authorId).ifPresent(authors::add);
        }
        return authors;
    }

}