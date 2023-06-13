package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.entity.BookAuthor;
import com.oksanatrifonova.bookshop.entity.Category;
import com.oksanatrifonova.bookshop.entity.Image;
import com.oksanatrifonova.bookshop.mapper.BookMapper;
import com.oksanatrifonova.bookshop.repository.BookAuthorRepository;
import com.oksanatrifonova.bookshop.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookMapper bookMapper;


    public List<BookDto> listAllActiveBooks() {
        return bookRepository.findByActive(true)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByCategory(Category category) {
        return bookRepository.findBooksByCategoryAndActive(category, true)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

    }

    public List<BookDto> findBooksByAuthor(Long authorId) {
        return bookAuthorRepository.findById(authorId)
                .map(author -> bookRepository.findBooksByAuthorsAndActive(author, true)
                        .stream()
                        .map(bookMapper::toDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public Set<BookAuthor> mapAuthorIdsToAuthors(List<Long> authorIds) {
        Set<BookAuthor> authors = new HashSet<>();
        for (Long authorId : authorIds) {
            BookAuthor author = bookAuthorRepository.findById(authorId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid author ID: " + authorId));
            authors.add(author);
        }
        return authors;
    }


    @Transactional
    public void saveBook(BookDto bookDto, MultipartFile file) throws IOException {
        Book book = bookMapper.toEntity(bookDto);
        book.setActive(true);

        Image image;
        if (!file.isEmpty()) {
            if (file.getSize() >= 65535) {
                throw new IllegalArgumentException("Image size exceeds the maximum allowed size");
            }
            image = toImageEntity(file);
            book.addImageToBook(image);
        }
        book.setAuthors(mapAuthorIdsToAuthors(bookDto.getAuthorIds()));

        bookRepository.save(book);
    }


    public Image toImageEntity(MultipartFile file) throws IOException {
        return new Image(
                file.getName(),
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getBytes()
        );
    }

    public void updateBookWithImage(BookDto updatedBookDto, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(updatedBookDto.getId()).orElse(null);
        if (book != null) {
            if (!file.isEmpty()) {
                if (file.getSize() <= 65535) {
                    Image existingImage = book.getImages();
                    Image updatedImage = toImageEntity(file);
                    if (existingImage != null) {
                        existingImage.setName(updatedImage.getName());
                        existingImage.setOriginalFileName(updatedImage.getOriginalFileName());
                        existingImage.setContentType(updatedImage.getContentType());
                        existingImage.setSize(updatedImage.getSize());
                        existingImage.setBytes(updatedImage.getBytes());
                    } else {
                        book.setImages(updatedImage);
                    }
                } else {
                    throw new IllegalArgumentException("Image size exceeds the maximum allowed size");
                }
            }
            book.setTitle(updatedBookDto.getTitle());
            book.setAuthors(mapAuthorIdsToAuthors(updatedBookDto.getAuthorIds()));
            book.setPrice(updatedBookDto.getPrice());
            book.setDescription(updatedBookDto.getDescription());
            book.setCategory(updatedBookDto.getCategory());
            bookRepository.save(book);
        }
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setActive(false);
            bookRepository.save(book);
        }
    }

    public void setBookToActive(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(null);
        if (book != null) {
            book.setActive(true);
            bookRepository.save(book);
        }

    }

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(null);
        if (book != null) {
            return bookMapper.toDto(book);
        }
        return null;
    }
}