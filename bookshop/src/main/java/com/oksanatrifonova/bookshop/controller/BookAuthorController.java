package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.service.BookAuthorService;
import com.oksanatrifonova.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/authors")
public class BookAuthorController {
    private final BookAuthorService bookAuthorService;
    private final BookService bookService;
    private static final String REDIRECT_TO_AUTHORS = "redirect:/authors";
    private static final String AUTHOR_ATTRIBUTE = "author";
    private static final String AUTHORS_TEMPLATE = "authors";
    private static final String BIRTH_YEAR_FIELD = "birthYear";
    private static final String AUTHOR_EDIT_TEMPLATE = "author-edit";
    private static final String DEATH_YEAR_FIELD = "deathYear";
    private static final String INVALID_YEAR = "invalid";


    @GetMapping
    public String getAllAuthors(Model model) {
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        model.addAttribute(AUTHORS_TEMPLATE, authors);
        model.addAttribute(AUTHOR_ATTRIBUTE, new BookAuthorDto());
        return AUTHORS_TEMPLATE;
    }

    @GetMapping("/{id}/books")
    public String showAllAuthorsBooks(@PathVariable Long id, Model model) {
        BookAuthorDto author = bookAuthorService.getAuthorById(id);
        List<BookDto> books = bookService.findBooksByAuthor(author.getId());
        model.addAttribute(AUTHOR_ATTRIBUTE, author);
        model.addAttribute("books", books);
        return "author-book";
    }

    @PostMapping
    public String addAuthor(@ModelAttribute(AUTHOR_ATTRIBUTE) BookAuthorDto author,
                            Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(AUTHOR_ATTRIBUTE, author);
            return AUTHORS_TEMPLATE;
        }

        if (author.getBirthYear() <= 0 || author.getBirthYear() > 2015) {
            bindingResult.rejectValue(BIRTH_YEAR_FIELD, INVALID_YEAR, "Invalid birth year");
            return AUTHORS_TEMPLATE;
        }
        if (author.getDeathYear() != null && author.getDeathYear() < 0 ||
                author.getDeathYear() != null && author.getDeathYear() > 2023 ||
                author.getDeathYear() != null && author.getBirthYear() > author.getDeathYear()) {
            bindingResult.rejectValue(DEATH_YEAR_FIELD, INVALID_YEAR, "Invalid death year");
            return AUTHORS_TEMPLATE;
        }
        bookAuthorService.createBookAuthor(author);
        return REDIRECT_TO_AUTHORS;
    }


    @PostMapping("/{authorId}/delete")
    public String deleteAuthor(@PathVariable Long authorId) {
        bookAuthorService.deleteBookAuthor(authorId);
        return REDIRECT_TO_AUTHORS;
    }

    @PostMapping("/{authorId}/edit")
    public String editAuthor(@PathVariable Long authorId,
                             @ModelAttribute("author") BookAuthorDto author,
                             Model model, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(AUTHOR_ATTRIBUTE, author);
            return AUTHOR_EDIT_TEMPLATE;
        }
        if (author.getBirthYear() <= 0 || author.getBirthYear() > 2015) {
            bindingResult.rejectValue(BIRTH_YEAR_FIELD, INVALID_YEAR, "Invalid birth year");
            return AUTHOR_EDIT_TEMPLATE;
        }

        if (author.getDeathYear() != null && (author.getDeathYear() <= 0 ||
                author.getDeathYear() > 2023 || author.getBirthYear() > author.getDeathYear())) {
            bindingResult.rejectValue(DEATH_YEAR_FIELD, INVALID_YEAR, "Invalid death year");
            return AUTHOR_EDIT_TEMPLATE;
        }
        bookAuthorService.editBookAuthor(authorId, author);
        return REDIRECT_TO_AUTHORS;
    }

    @GetMapping("/{authorId}/edit")
    public String showEditAuthorForm(@PathVariable Long authorId, Model model) {
        BookAuthorDto author = bookAuthorService.getAuthorById(authorId);
        model.addAttribute(AUTHOR_ATTRIBUTE, author);
        return AUTHOR_EDIT_TEMPLATE;
    }

}

