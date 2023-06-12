package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.entity.BookAuthor;
import com.oksanatrifonova.bookshop.entity.Category;
import com.oksanatrifonova.bookshop.entity.Images;
import com.oksanatrifonova.bookshop.mapper.BookMapper;
import com.oksanatrifonova.bookshop.repository.BookAuthorRepository;
import com.oksanatrifonova.bookshop.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookMapper bookMapper;


    public List<BookDto> listAllActiveBooks() {
        List<Book> activeBooks = bookRepository.findByActive(true);
        return activeBooks.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByCategory(Category category) {
        List<Book> books = bookRepository.findBooksByCategoryAndActive(category, true);
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

    }

    public List<BookDto> findBooksByAuthor(Long authorId) {
        BookAuthor author = bookAuthorRepository.findById(authorId).orElse(null);
        if (author == null) {
            return Collections.emptyList();
        }
        List<Book> books = bookRepository.findBooksByAuthorsAndActive(author, true);
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveBook(BookDto bookDto, MultipartFile file, List<Long> authorIds) throws IOException {
        Book book = bookMapper.toEntity(bookDto);
        book.setActive(true);

        Images image;
        if (!file.isEmpty()) {
            image = toImageEntity(file);
            book.addImageToBook(image);
        }
        Set<BookAuthor> authors = bookMapper.mapAuthorIdsToAuthors(authorIds);
        book.addAuthors(authors);
        bookRepository.save(book);
    }

    public Images toImageEntity(MultipartFile file) throws IOException {
        Images image = new Images();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }


    public void updateBookWithImage(BookDto updatedBookDto, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(updatedBookDto.getId()).orElse(null);
        if (book != null) {
            if (!file.isEmpty()) {
                Images existingImage = book.getImages();
                if (existingImage != null) {
                    existingImage.setName(file.getName());
                    existingImage.setOriginalFileName(file.getOriginalFilename());
                    existingImage.setContentType(file.getContentType());
                    existingImage.setSize(file.getSize());
                    existingImage.setBytes(file.getBytes());
                } else {
                    Images image = toImageEntity(file);
                    book.setImages(image);
                }
            }
            book.setTitle(updatedBookDto.getTitle());
            book.setAuthors(bookMapper.mapAuthorIdsToAuthors(updatedBookDto.getAuthorIds()));
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