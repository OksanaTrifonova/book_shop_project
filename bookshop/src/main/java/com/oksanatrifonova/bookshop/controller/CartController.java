package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
public class CartController {
    private static final String REDIRECT_TO_CART = "redirect:/cart";
    private static final String REDIRECT_TO_BOOKS = "redirect:/books";
    private static final String CART_ITEM = "cartItemCount";
    private static final String TOTAL_AMOUNT = "totalAmount";
    private static final String CART_TEMPLATE = "cart";
    private static final String AUTHORS = "authors";

    private Cart cart;
    private final AuthorService bookAuthorService;


    @GetMapping("/cart")
    public String viewCart(Model model) {
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM, cartItemCount);
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        BigDecimal totalAmount = cart.calculateTotalAmount();
        model.addAttribute(TOTAL_AMOUNT, totalAmount.toString());
        model.addAttribute(CART_TEMPLATE, cart.getItemDtoList());
        model.addAttribute(AUTHORS, authors);
        return CART_TEMPLATE;
    }

    @GetMapping("/book/{id}/cart")
    public String addToCart(@PathVariable("id") Long id, Model model) {
        cart.addToCart(id);
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM, cartItemCount);
        return REDIRECT_TO_BOOKS;
    }

    @PostMapping("/cart/remove/{id}")
    public String removeItemById(@PathVariable("id") Long id) {
        cart.removeItemById(id);
        return REDIRECT_TO_CART;
    }

    @PostMapping("/cart/removeAll")
    public String removeAllItems() {
        cart.removeAllItems();
        return REDIRECT_TO_CART;
    }

    @PostMapping("/cart/update/{id}")
    public String updateCartItem(@PathVariable("id") Long id, @RequestParam("quantity") int quantity) {
        cart.update(id, quantity);
        cart.calculateTotalAmount();
        return REDIRECT_TO_CART;
    }
}

