package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.entity.BookAuthor;
import com.oksanatrifonova.bookshop.mapper.BookAuthorMapper;
import com.oksanatrifonova.bookshop.repository.BookAuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookAuthorService {
    private static final String AUTHOR_NOT_FOUND_MSG = "Author not found";
    private final BookAuthorRepository bookAuthorRepository;
    private final BookAuthorMapper bookAuthorMapper;


    public void createBookAuthor(BookAuthorDto author) {
        BookAuthor bookAuthor = new BookAuthor(author.getName(), author.getBirthYear(), author.getDeathYear());
        bookAuthor.setActive(true);
        bookAuthor = bookAuthorRepository.save(bookAuthor);
        bookAuthorMapper.toDto(bookAuthor);
    }

    public void editBookAuthor(Long authorId, BookAuthorDto author) {
        BookAuthor bookAuthor = bookAuthorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(AUTHOR_NOT_FOUND_MSG));
        bookAuthor.setName(author.getName());
        bookAuthor.setBirthYear(author.getBirthYear());
        bookAuthor.setDeathYear(author.getDeathYear());
        bookAuthor.setActive(true);
        bookAuthorRepository.save(bookAuthor);
    }


    public void deleteBookAuthor(Long id) {
        BookAuthor bookAuthor = bookAuthorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(AUTHOR_NOT_FOUND_MSG));
        bookAuthor.setActive(false);
        bookAuthorRepository.save(bookAuthor);
    }

    public List<BookAuthorDto> getAllBookAuthors() {
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByActive(true);
        return bookAuthors.stream()
                .map(bookAuthorMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookAuthorDto getAuthorById(Long id) {
        BookAuthor author = bookAuthorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(AUTHOR_NOT_FOUND_MSG));
        return bookAuthorMapper.toDto(author);
    }
}
