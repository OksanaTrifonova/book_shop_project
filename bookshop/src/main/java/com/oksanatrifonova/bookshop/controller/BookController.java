package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.dto.ItemDto;
import com.oksanatrifonova.bookshop.entity.Category;
import com.oksanatrifonova.bookshop.service.BookAuthorService;
import com.oksanatrifonova.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@AllArgsConstructor
public class BookController {
    public static final String CART_ITEM_COUNT = "cartItemCount";
    public static final String BOOKS = "books";
    public static final String REDIRECT_BOOKS = "redirect:/" + BOOKS;
    public static final String AUTHORS = "authors";
    private final BookService bookService;
    private final Cart cart;
    private final BookAuthorService bookAuthorService;


    @GetMapping("/books")
    public String bookMain(@RequestParam(name = "filterButton", required = false) String filterButton,
                           @RequestParam(required = false) Long authorId,
                           Model model) {
        List<Category> categories = Arrays.asList(Category.values());
        model.addAttribute("categories", categories);
        int cartItemCount = cart.getItemDtoList().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);
        List<BookDto> books;
        if (filterButton != null && filterButton.equals("All")) {
            books = bookService.listAllActiveBooks();
        } else if (authorId != null) {
            books = bookService.findBooksByAuthor(authorId);
        } else {
            books = bookService.listAllActiveBooks();
        }
        model.addAttribute(BOOKS, books);
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute(AUTHORS, authors);
        return BOOKS;
    }

    @GetMapping("/books/category/{category}")
    public String getBooksByCategories(@PathVariable Category category, Model model) {
        int cartItemCount = cart.getItemDtoList().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);
        List<BookDto> books = bookService.findBooksByCategory(category);
        model.addAttribute(BOOKS, books);
        List<Category> categories = Arrays.asList(Category.values());
        model.addAttribute("categories", categories);
        return BOOKS;
    }

    @GetMapping("/book/add")
    public String bookAddForm(Model model) {
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        BookDto bookDto = new BookDto();
        model.addAttribute("book", bookDto);
        bookDto.setAuthorIds(new ArrayList<>());
        model.addAttribute(AUTHORS, authors);
        return "book-add";
    }

    @PostMapping("/book/add")
    public String bookAddPost(@RequestParam("file") MultipartFile file,
                              @Validated BookDto bookDto, BindingResult bindingResult, Model model) throws IOException {
        if (bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
            bindingResult.rejectValue("title", "error.book.title", "Title is required");
        }

        if (bookDto.getPrice() == null || bookDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            bindingResult.rejectValue("price", "error.book.price", "Price is required");
        }
        if (bookDto.getDescription() == null || bookDto.getDescription().isEmpty()) {
            bindingResult.rejectValue("description", "error.book.description", "Description is required");
        }

        if (bindingResult.hasErrors()) {
            List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
            model.addAttribute("book", bookDto);
            model.addAttribute(AUTHORS, authors);
            return "book-add";
        }
        bookService.saveBook(bookDto, file);
        return REDIRECT_BOOKS;
    }

    @GetMapping("/book/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        int cartItemCount = cart.getItemDtoList().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);
        BookDto bookDto = bookService.getBookById(id);
        model.addAttribute("book", bookDto);
        model.addAttribute(AUTHORS, authors);
        return "book-info";
    }

    @PostMapping("/book/{id}/remove")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "REDIRECT_BOOKS";
    }

    @PostMapping("/book/{id}/active")
    public String activeBook(@PathVariable Long id) {
        bookService.setBookToActive(id);
        return REDIRECT_BOOKS;
    }


    @GetMapping("/book/{id}/edit")
    public String showBookEditForm(@PathVariable(value = "id") Long id, Model model) {
        BookDto bookDto = bookService.getBookById(id);
        if (bookDto == null) {
            return REDIRECT_BOOKS;
        }
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute("book", bookDto);
        model.addAttribute(AUTHORS, authors);

        return "book-edit";
    }

    @PostMapping("/book/{id}/edit")
    public String bookUpdate(@Validated @ModelAttribute("book") BookDto updatedBookDto,
                             BindingResult bindingResult, Model model,
                             @RequestParam("imageFile") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
            model.addAttribute("book", updatedBookDto);
            model.addAttribute(AUTHORS, authors);
            return "book-edit";
        }
        bookService.updateBookWithImage(updatedBookDto, file);
        return REDIRECT_BOOKS;
    }

    @GetMapping("/books/filter")
    public String filterBooksByCategory(@RequestParam("category") Category category, Model model) {
        List<BookDto> filteredBooks = bookService.findBooksByCategory(category);
        model.addAttribute(BOOKS, filteredBooks);
        return BOOKS;
    }

}