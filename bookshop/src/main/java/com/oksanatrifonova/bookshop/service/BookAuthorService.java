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

    private final BookAuthorRepository bookAuthorRepository;
    private final BookAuthorMapper bookAuthorMapper;


    public BookAuthorDto createBookAuthor(String name, Integer birthYear, Integer deathYear) {
        BookAuthor bookAuthor = new BookAuthor(name,birthYear,deathYear);
        bookAuthor.setActive(true);
        bookAuthor = bookAuthorRepository.save(bookAuthor);
        return bookAuthorMapper.toDto(bookAuthor);
    }
    public void editBookAuthor(Long authorId, String name, Integer birthYear, Integer deathYear) {
        BookAuthor bookAuthor = bookAuthorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        bookAuthor.setName(name);
        bookAuthor.setBirthYear(birthYear);
        bookAuthor.setDeathYear(deathYear);
        bookAuthor = bookAuthorRepository.save(bookAuthor);
        bookAuthorMapper.toDto(bookAuthor);
    }


    public void deleteBookAuthor(Long id) {
        BookAuthor bookAuthor = bookAuthorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        if (bookAuthor!=null){
        bookAuthor.setActive(false);
        bookAuthorRepository.save(bookAuthor);
        }
    }

    public List<BookAuthorDto> getAllBookAuthors() {
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByActive(true);
        return bookAuthors.stream()
                .map(bookAuthorMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookAuthorDto getAuthorById(Long id){
        BookAuthor author = bookAuthorRepository.getById(id);
        return bookAuthorMapper.toDto(author);
    }
}
