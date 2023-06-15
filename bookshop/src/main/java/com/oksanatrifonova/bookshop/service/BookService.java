package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.entity.Author;
import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.entity.Category;
import com.oksanatrifonova.bookshop.entity.Image;
import com.oksanatrifonova.bookshop.exception.BookshopException;
import com.oksanatrifonova.bookshop.mapper.BookMapper;
import com.oksanatrifonova.bookshop.repository.AuthorRepository;
import com.oksanatrifonova.bookshop.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private static final String INVALID_ID_MSG = "Invalid author ID: ";
    private static final String IMAGE_SIZE_MSG = "Image size exceeds the maximum allowed size";
    private static final String TITLE_MSG = "Title is required";
    private static final String PRICE_MSG = "Price is required";
    private static final String PRICE_VALIDATION_MSG = "Price must be greater than 0";
    private static final String DESCRIPTION_MSG = "Description is required";

    private final BookRepository bookRepository;
    private final AuthorRepository bookAuthorRepository;
    private final BookMapper bookMapper;

    public Page<BookDto> getBooksByFilter(String filterButton, Long authorId, Pageable pageable) {
        if (filterButton != null && filterButton.equals("All")) {
            return listAllActiveBooks(pageable);
        } else if (authorId != null) {
            return findBooksByAuthor(authorId, pageable);
        } else {
            return listAllActiveBooks(pageable);
        }
    }

    public Page<BookDto> listAllActiveBooks(Pageable pageable) {
        return bookRepository.findByActive(true, pageable)
                .map(bookMapper::toDto);
    }

    public Page<BookDto> findBooksByAuthor(Long authorId, Pageable pageable) {
        return bookAuthorRepository.findById(authorId)
                .map(author -> bookRepository.findBooksByAuthorsAndActive(author, true, pageable))
                .orElse(Page.empty())
                .map(bookMapper::toDto);
    }

    public List<BookDto> findBooksByAuthorFor(Long authorId) {
        Author author = bookAuthorRepository.findById(authorId)
                .orElseThrow(() -> new BookshopException(INVALID_ID_MSG + authorId));

        List<Book> books = bookRepository.findAllByAuthors(author);
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public List<String> getAllCategoryNames() {
        return Arrays.stream(Category.values())
                .map(Category::toString)
                .toList();
    }

    public Page<BookDto> findBooksByCategory(Category category, Pageable pageable) {
        Page<Book> bookPage = bookRepository.findBooksByCategoryAndActive(category, true, pageable);
        return bookPage.map(bookMapper::toDto);
    }

    public Set<Author> mapAuthorIdsToAuthors(List<Long> authorIds) {
        return authorIds.stream()
                .map(authorId -> bookAuthorRepository.findById(authorId)
                        .orElseThrow(() -> new BookshopException(INVALID_ID_MSG + authorId)))
                .collect(Collectors.toSet());
    }

    @Transactional
    public void saveBook(BookDto bookDto, MultipartFile file) throws BookshopException, IOException {
        validateBook(bookDto);
        Book book = bookMapper.toEntity(bookDto);
        book.setActive(true);

        Image image;
        if (!file.isEmpty()) {
            if (file.getSize() >= 65535) {
                throw new BookshopException(IMAGE_SIZE_MSG);
            }
            image = toImageEntity(file);
            book.addImageToBook(image);
        }
        book.setAuthors(mapAuthorIdsToAuthors(bookDto.getAuthorIds()));

        bookRepository.save(book);
    }

    private void validateBook(BookDto bookDto) {
        if (bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
            throw new BookshopException(TITLE_MSG);
        }
        if (bookDto.getPrice() == null) {
            throw new BookshopException(PRICE_MSG);
        }
        if (bookDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BookshopException(PRICE_VALIDATION_MSG);
        }

        if (bookDto.getDescription() == null || bookDto.getDescription().isEmpty()) {
            throw new BookshopException(DESCRIPTION_MSG);
        }
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
        validateBook(updatedBookDto);
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
                    throw new BookshopException(IMAGE_SIZE_MSG);
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