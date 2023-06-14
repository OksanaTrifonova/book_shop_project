package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.exception.BookValidationException;
import com.oksanatrifonova.bookshop.service.AuthorService;
import com.oksanatrifonova.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private static final String REDIRECT_TO_AUTHORS = "redirect:/authors";
    private static final String AUTHOR_ATTRIBUTE = "author";
    private static final String AUTHORS_TEMPLATE = "authors";
    private static final String AUTHORS_BOOK = "author-book";
    private static final String ERROR = "error";
    private static final String AUTHOR_EDIT_TEMPLATE = "author-edit";
    private static final String AUTHOR_ADD = "author-add";
    private static final String BOOKS = "books";
    private final AuthorService bookAuthorService;
    private final BookService bookService;


    @GetMapping
    public String getAllAuthors(Model model) {

        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute(AUTHORS_TEMPLATE, authors);
        model.addAttribute(AUTHOR_ATTRIBUTE, new BookAuthorDto());
        return AUTHORS_TEMPLATE;
    }

    @GetMapping("/add")
    public String viewUserAddForm(Model model) {
        if (model.containsAttribute(ERROR)) {
            String error = (String) model.getAttribute(ERROR);
            model.addAttribute(ERROR, error);
        }
        BookAuthorDto authorDto = new BookAuthorDto();
        model.addAttribute(AUTHOR_ATTRIBUTE, authorDto);
        return AUTHOR_ADD;
    }

    @GetMapping("/{id}/books")
    public String showAllAuthorsBooks(@PathVariable Long id, Model model) {
        BookAuthorDto author = bookAuthorService.getAuthorById(id);
        model.addAttribute(AUTHOR_ATTRIBUTE, author);
        List<BookDto> books = bookService.findBooksByAuthorFor(author.getId());
        model.addAttribute(AUTHOR_ATTRIBUTE, author);
        model.addAttribute(BOOKS, books);
        return AUTHORS_BOOK;
    }

    @PostMapping("/add")
    public String addAuthor(@ModelAttribute(AUTHOR_ATTRIBUTE) BookAuthorDto author,
                            Model model) {
        try {
            bookAuthorService.createBookAuthor(author);
            return REDIRECT_TO_AUTHORS;
        } catch (BookValidationException e) {
            model.addAttribute(ERROR, e.getMessage());
            return AUTHOR_ADD;
        }
    }


    @PostMapping("/{authorId}/delete")
    public String deleteAuthor(@PathVariable Long authorId) {
        bookAuthorService.deleteBookAuthor(authorId);
        return REDIRECT_TO_AUTHORS;
    }

    @PostMapping("/{authorId}/edit")
    public String editAuthor(@PathVariable Long authorId,
                             @ModelAttribute(AUTHOR_ATTRIBUTE) BookAuthorDto author,
                             Model model) {
        try {
            bookAuthorService.editBookAuthor(authorId, author);
            return REDIRECT_TO_AUTHORS;
        } catch (BookValidationException e) {
            model.addAttribute(ERROR, e.getMessage());
            return AUTHORS_TEMPLATE;
        }
    }

    @GetMapping("/{authorId}/edit")
    public String showEditAuthorForm(@PathVariable Long authorId, Model model) {
        BookAuthorDto author = bookAuthorService.getAuthorById(authorId);
        model.addAttribute(AUTHOR_ATTRIBUTE, author);
        return AUTHOR_EDIT_TEMPLATE;
    }

}

