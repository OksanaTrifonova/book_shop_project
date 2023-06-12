package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.service.BookAuthorService;
import com.oksanatrifonova.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Controller
@AllArgsConstructor
public class BookAuthorController {
    private final BookAuthorService bookAuthorService;
    private final BookService bookService;

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute("authors", authors);
        return "authors";
    }
    @GetMapping("/authors/{id}/books")
    public String showAllAuthorsBooks(@PathVariable Long id, Model model){
    BookAuthorDto author = bookAuthorService.getAuthorById(id);
    List<BookDto> books = bookService.findBooksByAuthor(author.getId());
    model.addAttribute("author",author);
    model.addAttribute("books",books);
    return "author-book";
    }

    @PostMapping("authors")
    public String addAuthor(@RequestParam String name,
                            @RequestParam @DateTimeFormat(pattern = "yyyy") Integer birthYear,
                            @RequestParam (required = false) @DateTimeFormat(pattern = "yyyy") Integer deathYear) {

        BookAuthorDto dto = bookAuthorService.createBookAuthor(name, birthYear, deathYear);
        return "redirect:/authors";
    }


    @PostMapping("/authors/{authorId}/delete")
    public String deleteAuthor(@PathVariable Long authorId) {
        bookAuthorService.deleteBookAuthor(authorId);
        return "redirect:/authors";
    }

    @PostMapping("/authors/{authorId}/edit")
    public String editAuthor(@PathVariable Long authorId,
                             @RequestParam String name,
                             @RequestParam Integer birthYear,
                             @RequestParam(required = false) Integer deathYear) {
        bookAuthorService.editBookAuthor(authorId, name, birthYear, deathYear);
        return "redirect:/authors";
    }
    @GetMapping("/authors/{authorId}/edit")
    public String showEditAuthorForm(@PathVariable Long authorId, Model model) {
        BookAuthorDto author = bookAuthorService.getAuthorById(authorId);
        model.addAttribute("author", author);
        return "author-edit";
    }

    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public String handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, Model model) {
            model.addAttribute("errorMessage", "Invalid ID format");
            model.addAttribute("errorDescription", "The provided ID is not valid. Please enter a valid ID.");
            return "error404";
        }
    }
}

//    @GetMapping("/authors")
//    public String getAuthors(Model model, @RequestParam(defaultValue = "10") int pageSize, Pageable pageable) {
//        Pageable pageableWithSize = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
//        Page<BookAuthorDto> authorsPage = bookAuthorService.getAllBookAuthorsByPage(pageableWithSize);
//        model.addAttribute("authors", authorsPage.getContent());
//        model.addAttribute("page", authorsPage);
//        return "authors";
//    }
