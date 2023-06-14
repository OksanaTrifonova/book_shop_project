package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.entity.BookAuthor;
import com.oksanatrifonova.bookshop.exception.BookValidationException;
import com.oksanatrifonova.bookshop.mapper.BookAuthorMapper;
import com.oksanatrifonova.bookshop.repository.BookAuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {
    private static final String AUTHOR_NOT_FOUND_MSG = "Author not found";
    private static final String WRONG_DEATH_YEAR_MSG = "Death year cannot be before birth year.";
    private static final String WRONG_BIRTH_YEAR_MSG ="Birth year cannot be in the future.";
    private static final String WRONG_DEATH_YEAR_FUTURE_MSG ="Death year cannot be in the future.";
    private final BookAuthorRepository bookAuthorRepository;
    private final BookAuthorMapper bookAuthorMapper;


    public void createBookAuthor(BookAuthorDto author) {
        Integer birthYear = parseYear(author.getBirthYear());
        Integer deathYear = parseYear(author.getDeathYear());

        validateBirthAndDeathYears(birthYear, deathYear);

        BookAuthor bookAuthor = new BookAuthor(author.getName(), birthYear, deathYear);
        bookAuthor.setActive(true);
        bookAuthor = bookAuthorRepository.save(bookAuthor);
        bookAuthorMapper.toDto(bookAuthor);
    }

    public void editBookAuthor(Long authorId, BookAuthorDto author) {
        BookAuthor bookAuthor = bookAuthorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(AUTHOR_NOT_FOUND_MSG));
        Integer birthYear = parseYear(author.getBirthYear());
        Integer deathYear = parseYear(author.getDeathYear());

        validateBirthAndDeathYears(birthYear, deathYear);

        bookAuthor.setName(author.getName());
        bookAuthor.setBirthYear(birthYear);
        bookAuthor.setDeathYear(deathYear);
        bookAuthor.setActive(true);
        bookAuthorRepository.save(bookAuthor);
    }

    private void validateBirthAndDeathYears(Integer birthYear, Integer deathYear) {
        if (birthYear != null && deathYear != null && deathYear < birthYear) {
            throw new BookValidationException(WRONG_DEATH_YEAR_MSG);
        }

        if (birthYear != null && birthYear > Year.now().getValue()) {
            throw new BookValidationException(WRONG_BIRTH_YEAR_MSG);
        }

        if (deathYear != null && deathYear > YearMonth.now().getYear()) {
            throw new BookValidationException(WRONG_DEATH_YEAR_FUTURE_MSG);
        }

    }


    private Integer parseYear(String yearString) {
        if (yearString == null || yearString.isBlank()) {
            return null;
        } else if (yearString.endsWith(" BC")) {
            int yearValue = Integer.parseInt(yearString.substring(0, yearString.indexOf(' ')));
            return -yearValue;
        } else {
            return Integer.parseInt(yearString);
        }
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
                .toList();
    }

    public BookAuthorDto getAuthorById(Long id) {
        BookAuthor author = bookAuthorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(AUTHOR_NOT_FOUND_MSG));
        return bookAuthorMapper.toDto(author);
    }
}
