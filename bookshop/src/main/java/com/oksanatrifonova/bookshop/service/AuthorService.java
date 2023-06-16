package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.AuthorDto;
import com.oksanatrifonova.bookshop.entity.Author;
import com.oksanatrifonova.bookshop.exception.BookshopException;
import com.oksanatrifonova.bookshop.mapper.AuthorMapper;
import com.oksanatrifonova.bookshop.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {
    private static final String AUTHOR_NOT_FOUND_MSG = "Author not found";
    private static final String WRONG_DEATH_YEAR_MSG = "Death year cannot be before birth year.";
    private static final String WRONG_BIRTH_YEAR_MSG = "Birth year cannot be in the future.";
    private static final String WRONG_DEATH_YEAR_FUTURE_MSG = "Death year cannot be in the future.";
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public void addAuthor(AuthorDto authorDto) {
        Integer birthYear = parseYear(authorDto.getBirthYear());
        Integer deathYear = parseYear(authorDto.getDeathYear());
        validateBirthAndDeathYears(birthYear, deathYear);
        Author author = new Author(authorDto.getName(), birthYear, deathYear);
        author.setActive(true);
        author = authorRepository.save(author);
        authorMapper.toDto(author);
    }

    public void editAuthor(Long authorId, AuthorDto authorDto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new BookshopException(AUTHOR_NOT_FOUND_MSG));
        Integer birthYear = parseYear(authorDto.getBirthYear());
        Integer deathYear = parseYear(authorDto.getDeathYear());
        validateBirthAndDeathYears(birthYear, deathYear);
        author.setName(authorDto.getName());
        author.setBirthYear(birthYear);
        author.setDeathYear(deathYear);
        author.setActive(true);
        authorRepository.save(author);
    }

    private void validateBirthAndDeathYears(Integer birthYear, Integer deathYear) {
        if (birthYear != null && deathYear != null && deathYear < birthYear) {
            throw new BookshopException(WRONG_DEATH_YEAR_MSG);
        }

        if (birthYear != null && birthYear > Year.now().getValue()) {
            throw new BookshopException(WRONG_BIRTH_YEAR_MSG);
        }

        if (deathYear != null && deathYear > Year.now().plusYears(1).getValue()) {
            throw new BookshopException(WRONG_DEATH_YEAR_FUTURE_MSG);
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

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new BookshopException(AUTHOR_NOT_FOUND_MSG));
        author.setActive(false);
        authorRepository.save(author);
    }

    public List<AuthorDto> getAllBookAuthors() {
        List<Author> authors = authorRepository.findByActive(true);
        return authors.stream()
                .map(authorMapper::toDto)
                .toList();
    }

    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new BookshopException(AUTHOR_NOT_FOUND_MSG));
        return authorMapper.toDto(author);
    }
}
