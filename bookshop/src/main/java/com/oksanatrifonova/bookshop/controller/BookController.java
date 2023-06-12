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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
@AllArgsConstructor
public class BookController {
    private final BookService bookService;
    private final Cart cart;
    private final BookAuthorService bookAuthorService;


    @GetMapping("/books")
    public String bookMain(@RequestParam(name = "filterButton", required = false) String filterButton,
                           @RequestParam(required = false) Long authorId,
                           Model model) {
        List<Category> categories = Arrays.asList(Category.values());
        model.addAttribute("categories", categories);
        int cartItemCount = cart.getCart().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        model.addAttribute("cartItemCount", cartItemCount);
        List<BookDto> books;
        if (filterButton != null && filterButton.equals("All")) {
            books = bookService.listAllActiveBooks();
        } else if (authorId != null) {
            books = bookService.findBooksByAuthor(authorId);
        } else {
            books = bookService.listAllActiveBooks();
        }
        model.addAttribute("books", books);
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute("authors", authors);
        return "books";
    }

    @GetMapping("/books/category/{category}")
    public String getBooksByCategories(@PathVariable Category category, Model model) {
        int cartItemCount = cart.getCart().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        model.addAttribute("cartItemCount", cartItemCount);
        List<BookDto> books = bookService.findBooksByCategory(category);
        model.addAttribute("books", books);
        List<Category> categories = Arrays.asList(Category.values());
        model.addAttribute("categories", categories);
        return "books";
    }

    @GetMapping("/book/add")
    public String bookAddForm(Model model) {
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        BookDto bookDto = new BookDto();
        model.addAttribute("book", bookDto);
        bookDto.setAuthorIds(new ArrayList<>());
        model.addAttribute("authors", authors);
        return "book-add";
    }

    @PostMapping("/book/add")
    public String bookAddPost(@RequestParam("file") MultipartFile file, @RequestParam("authorIds") List<Long> authorIds,
                              @Validated BookDto bookDto, BindingResult bindingResult, Model model) throws IOException {
        if (bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
            bindingResult.rejectValue("title", "error.book.title", "Title is required");
        }

        if (bookDto.getPrice() == null || bookDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            bindingResult.rejectValue("price", "error.book.price", "Price is required");
        }
        if (bookDto.getDescription()==null || bookDto.getDescription().isEmpty()) {
            bindingResult.rejectValue("description", "error.book.description", "Description is required");
        }
        for (ObjectError error : bindingResult.getAllErrors()) {
            System.out.println(error.getDefaultMessage());
        }

        if (bindingResult.hasErrors()) {
            List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
            model.addAttribute("book", bookDto);
            model.addAttribute("authors", authors);
            return "book-add";
        }
        bookService.saveBook(bookDto, file, authorIds);
        return "redirect:/books";
    }
    @GetMapping("/book/{id}")
    public String bookDetails(@PathVariable Long id, Model model) {
        int cartItemCount = cart.getCart().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute("cartItemCount", cartItemCount);
        BookDto bookDto = bookService.getBookById(id);
        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authors);
        return "book-info";
    }

    @PostMapping("/book/{id}/remove")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
    @PostMapping("/book/{id}/active")
    public String activeBook(@PathVariable Long id) {
        bookService.setBookToActive(id);
        return "redirect:/books";
    }


    @GetMapping("/book/{id}/edit")
    public String showBookEditForm(@PathVariable(value = "id") Long id, Model model) {
        BookDto bookDto = bookService.getBookById(id);
        if (bookDto == null) {
            return "redirect:/books";
        }
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authors);
        return "book-edit";
    }

    @PostMapping("/book/{id}/edit")
    public String bookUpdate(@Validated @ModelAttribute("book") BookDto updatedBookDto,
                             BindingResult bindingResult, Model model,
                             @RequestParam("imageFile") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
            model.addAttribute("book", updatedBookDto);
            model.addAttribute("authors", authors);
            return "book-edit";
        }
        bookService.updateBookWithImage(updatedBookDto, file);
        return "redirect:/books";
    }

    @GetMapping("/books/filter")
    public String filterBooksByCategory(@RequestParam("category") Category category, Model model) {
        List<BookDto> filteredBooks = bookService.findBooksByCategory(category);
        model.addAttribute("books", filteredBooks);
        return "books";
    }

}
