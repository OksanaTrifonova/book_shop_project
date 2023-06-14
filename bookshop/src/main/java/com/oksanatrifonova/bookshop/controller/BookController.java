package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.entity.Category;
import com.oksanatrifonova.bookshop.exception.BookValidationException;
import com.oksanatrifonova.bookshop.service.AuthorService;
import com.oksanatrifonova.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class BookController {
    public static final String CART_ITEM_COUNT = "cartItemCount";
    public static final String BOOKS = "books";
    public static final String BOOK = "book";
    public static final String BOOK_EDIT = "book-edit";
    public static final String REDIRECT_BOOKS = "redirect:/" + BOOKS;
    public static final String AUTHORS = "authors";
    public static final String CATEGORIES = "categories";
    public static final String BOOK_INFO = "book-info";
    public static final String BOOK_ADD = "book-add";
    public static final String ERROR = "error";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String TOTAL_PAGES = "totalPages";
    public static final String PREVIOUS_PAGE = "hasPreviousPage";
    public static final String NEXT_PAGE = "hasNextPage";
    public static final String IMAGE_FILE = "imageFile";

    private final BookService bookService;
    private final Cart cart;
    private final AuthorService bookAuthorService;


    @GetMapping("/books")
    public String bookMain(@RequestParam(name = "filterButton", required = false) String filterButton,
                           @RequestParam(required = false) Long authorId,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        PageRequest pageRequest = PageRequest.of(page, 6);
        List<Category> categories = Arrays.asList(Category.values());
        model.addAttribute(CATEGORIES, categories);
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);

        Page<BookDto> bookPage = bookService.getBooksByFilter(filterButton, authorId, pageRequest);
        List<BookDto> books = bookPage.getContent();
        model.addAttribute(BOOKS, books);
        model.addAttribute(CURRENT_PAGE, page);
        model.addAttribute(TOTAL_PAGES, bookPage.getTotalPages());
        model.addAttribute(PREVIOUS_PAGE, bookPage.hasPrevious());
        model.addAttribute(NEXT_PAGE, bookPage.hasNext());
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute(AUTHORS, authors);

        return BOOKS;
    }

    @GetMapping("/books/category/{category}")
    public String getBooksByCategories(@PathVariable Category category,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       Model model) {
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);

        Pageable pageable = PageRequest.of(page, 6);
        Page<BookDto> bookPage = bookService.findBooksByCategory(category, pageable);

        model.addAttribute(BOOKS, bookPage.getContent());
        model.addAttribute(CATEGORIES, Arrays.asList(Category.values()));
        model.addAttribute(CURRENT_PAGE, bookPage.getNumber());
        model.addAttribute(TOTAL_PAGES, bookPage.getTotalPages());
        model.addAttribute(PREVIOUS_PAGE, bookPage.hasPrevious());
        model.addAttribute(NEXT_PAGE, bookPage.hasNext());

        return BOOKS;
    }

    @GetMapping("/book/add")
    public String bookAddForm(Model model) {
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        BookDto bookDto = new BookDto();
        model.addAttribute(BOOK, bookDto);
        bookDto.setAuthorIds(new ArrayList<>());
        model.addAttribute(AUTHORS, authors);
        return BOOK_ADD;
    }

    @PostMapping("/book/add")
    public String bookAddPost(@RequestParam("file") MultipartFile file,
                              @Validated BookDto bookDto, Model model) throws IOException {
        try {
            bookService.saveBook(bookDto, file);
            return REDIRECT_BOOKS;
        } catch (BookValidationException e) {
            List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
            model.addAttribute(ERROR, e.getMessage());
            model.addAttribute(BOOK, bookDto);
            model.addAttribute(AUTHORS, authors);
            return BOOK_ADD;
        }

    }

    @GetMapping("/book/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute(AUTHORS, authors);
        BookDto bookDto = bookService.getBookById(id);
        model.addAttribute(BOOK, bookDto);
        return BOOK_INFO;
    }

    @PostMapping("/book/{id}/remove")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return REDIRECT_BOOKS;
    }

    @PostMapping("/book/{id}/active")
    public String activeBook(@PathVariable Long id) {
        bookService.setBookToActive(id);
        return REDIRECT_BOOKS;
    }


    @GetMapping("/book/{id}/edit")
    public String showBookEditForm(@PathVariable(value = "id") Long id, Model model) {
        BookDto bookDto = bookService.getBookById(id);
        model.addAttribute(BOOK, bookDto);
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute(AUTHORS, authors);
        return BOOK_EDIT;
    }

    @PostMapping("/book/{id}/edit")
    public String bookUpdate(@Validated @ModelAttribute(BOOK) BookDto updatedBookDto,
                             Model model,
                             @RequestParam(IMAGE_FILE) MultipartFile file) throws IOException {
        try {
            bookService.updateBookWithImage(updatedBookDto, file);
            return REDIRECT_BOOKS;

        } catch (BookValidationException e) {
            model.addAttribute(ERROR, e.getMessage());
            model.addAttribute(BOOK, updatedBookDto);
            List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
            model.addAttribute(AUTHORS, authors);
            return BOOK_EDIT;
        }
    }
}