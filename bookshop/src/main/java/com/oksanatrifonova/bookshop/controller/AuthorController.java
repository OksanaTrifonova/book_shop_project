package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.AuthorDto;
import com.oksanatrifonova.bookshop.exception.BookshopException;
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
    private final AuthorService authorService;
    private final BookService bookService;

    @GetMapping
    public String getAllAuthors(Model model) {
        model.addAttribute(AUTHORS_TEMPLATE, authorService.getAllBookAuthors());
        model.addAttribute(AUTHOR_ATTRIBUTE, new AuthorDto());
        return AUTHORS_TEMPLATE;
    }

    @GetMapping("/add")
    public String viewAuthorAddForm(Model model) {
        if (model.containsAttribute(ERROR)) {
            model.addAttribute(ERROR, model.getAttribute(ERROR));
        }
        model.addAttribute(AUTHOR_ATTRIBUTE, new AuthorDto());
        return AUTHOR_ADD;
    }

    @GetMapping("/{id}/books")
    public String showAllAuthorsBooks(@PathVariable Long id, Model model) {
        model.addAttribute(AUTHOR_ATTRIBUTE, authorService.getAuthorById(id));
        model.addAttribute(BOOKS, bookService.findBooksByAuthorFor(authorService.getAuthorById(id).getId()));
        return AUTHORS_BOOK;
    }

    @PostMapping("/add")
    public String addAuthor(@ModelAttribute(AUTHOR_ATTRIBUTE) AuthorDto author,
                            Model model) {
        try {
            authorService.addAuthor(author);
            return REDIRECT_TO_AUTHORS;
        } catch (BookshopException e) {
            model.addAttribute(ERROR, e.getMessage());
            return AUTHOR_ADD;
        }
    }

    @PostMapping("/{authorId}/delete")
    public String deleteAuthor(@PathVariable Long authorId) {
        authorService.deleteAuthor(authorId);
        return REDIRECT_TO_AUTHORS;
    }

    @PostMapping("/{authorId}/edit")
    public String editAuthor(@PathVariable Long authorId,
                             @ModelAttribute(AUTHOR_ATTRIBUTE) AuthorDto authorDto,
                             Model model) {
        try {
            authorService.editAuthor(authorId, authorDto);
            return REDIRECT_TO_AUTHORS;
        } catch (BookshopException e) {
            model.addAttribute(ERROR, e.getMessage());
            return AUTHORS_TEMPLATE;
        }
    }

    @GetMapping("/{authorId}/edit")
    public String showEditAuthorForm(@PathVariable Long authorId, Model model) {
        model.addAttribute(AUTHOR_ATTRIBUTE, authorService.getAuthorById(authorId));
        return AUTHOR_EDIT_TEMPLATE;
    }
}

