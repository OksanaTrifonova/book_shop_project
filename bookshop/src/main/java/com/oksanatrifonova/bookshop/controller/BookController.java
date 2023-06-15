package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.entity.Category;
import com.oksanatrifonova.bookshop.exception.BookshopException;
import com.oksanatrifonova.bookshop.service.AuthorService;
import com.oksanatrifonova.bookshop.service.BookService;
import lombok.AllArgsConstructor;
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
    private final AuthorService authorService;

    @GetMapping("/books")
    public String bookMain(@RequestParam(name = "filterButton", required = false) String filterButton,
                           @RequestParam(required = false) Long authorId,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        PageRequest pageRequest = PageRequest.of(page, 6);
        model.addAttribute(CATEGORIES, bookService.getAllCategoryNames());
        model.addAttribute(CART_ITEM_COUNT, cart.getItemCount());
        model.addAttribute(BOOKS, bookService.getBooksByFilter(filterButton, authorId, pageRequest).getContent());
        model.addAttribute(CURRENT_PAGE, page);
        model.addAttribute(TOTAL_PAGES, bookService.getBooksByFilter(filterButton, authorId, pageRequest).getTotalPages());
        model.addAttribute(PREVIOUS_PAGE, bookService.getBooksByFilter(filterButton, authorId, pageRequest).hasPrevious());
        model.addAttribute(NEXT_PAGE, bookService.getBooksByFilter(filterButton, authorId, pageRequest).hasNext());
        model.addAttribute(AUTHORS, authorService.getAllBookAuthors());
        return BOOKS;
    }

    @GetMapping("/books/category/{category}")
    public String getBooksByCategories(@PathVariable Category category,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       Model model) {
        Pageable pageable = PageRequest.of(page, 6);
        model.addAttribute(CART_ITEM_COUNT, cart.getItemCount());
        model.addAttribute(BOOKS, bookService.findBooksByCategory(category, pageable).getContent());
        model.addAttribute(CATEGORIES, bookService.getAllCategoryNames());
        model.addAttribute(CURRENT_PAGE, bookService.findBooksByCategory(category, pageable).getNumber());
        model.addAttribute(TOTAL_PAGES, bookService.findBooksByCategory(category, pageable).getTotalPages());
        model.addAttribute(PREVIOUS_PAGE, bookService.findBooksByCategory(category, pageable).hasPrevious());
        model.addAttribute(NEXT_PAGE, bookService.findBooksByCategory(category, pageable).hasNext());
        return BOOKS;
    }

    @GetMapping("/book/add")
    public String bookAddForm(Model model) {
        model.addAttribute(BOOK, new BookDto());
        model.addAttribute(AUTHORS, authorService.getAllBookAuthors());
        return BOOK_ADD;
    }

    @PostMapping("/book/add")
    public String bookAddPost(@RequestParam("file") MultipartFile file,
                              @Validated BookDto bookDto, Model model) throws IOException {
        try {
            bookService.saveBook(bookDto, file);
            return REDIRECT_BOOKS;
        } catch (BookshopException e) {
            model.addAttribute(ERROR, e.getMessage());
            model.addAttribute(BOOK, bookDto);
            model.addAttribute(AUTHORS, authorService.getAllBookAuthors());
            return BOOK_ADD;
        }
    }

    @GetMapping("/book/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        model.addAttribute(CART_ITEM_COUNT, cart.getItemCount());
        model.addAttribute(AUTHORS, authorService.getAllBookAuthors());
        model.addAttribute(BOOK, bookService.getBookById(id));
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
        model.addAttribute(BOOK, bookService.getBookById(id));
        model.addAttribute(AUTHORS, authorService.getAllBookAuthors());
        return BOOK_EDIT;
    }

    @PostMapping("/book/{id}/edit")
    public String bookUpdate(@Validated @ModelAttribute(BOOK) BookDto updatedBookDto,
                             Model model,
                             @RequestParam(IMAGE_FILE) MultipartFile file) throws IOException {
        try {
            bookService.updateBookWithImage(updatedBookDto, file);
            return REDIRECT_BOOKS;

        } catch (BookshopException e) {
            model.addAttribute(ERROR, e.getMessage());
            model.addAttribute(BOOK, updatedBookDto);
            model.addAttribute(AUTHORS, authorService.getAllBookAuthors());
            return BOOK_EDIT;
        }
    }
}