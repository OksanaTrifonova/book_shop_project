package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.BookAuthorDto;
import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.dto.ItemDto;
import com.oksanatrifonova.bookshop.service.BookAuthorService;
import com.oksanatrifonova.bookshop.service.BookService;
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
    private BookService bookService;
    private Cart cart;
    private final BookAuthorService bookAuthorService;


    @GetMapping("/cart")
    public String viewCart(Model model) {
        int cartItemCount = cart.getCart().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        model.addAttribute("cartItemCount", cartItemCount);
        List<BookAuthorDto> authors = bookAuthorService.getAllBookAuthors();
        BigDecimal totalAmount = cart.calculateTotalAmount();
        model.addAttribute("totalAmount", totalAmount.toString());
        model.addAttribute("cart", cart.getCart());
        model.addAttribute("authors", authors);
        return "cart";
    }

    @GetMapping("/book/{id}/cart")
    public String addToCart(@PathVariable("id") Long id, Model model) {
        BookDto book = bookService.getBookById(id);
        if (book != null) {
            cart.addItemToCart(book, 1, book.getPrice());
            cart.calculateTotalAmount();
            int cartItemCount = cart.getCart().stream()
                    .mapToInt(ItemDto::getQuantity)
                    .sum();
            model.addAttribute("cartItemCount", cartItemCount);
        }
        return "redirect:/books";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeItemById(@PathVariable("id") Long id) {
        cart.removeItemById(id);

        return "redirect:/cart";
    }

    @PostMapping("/cart/removeAll")
    public String removeAllItems() {
        cart.removeAllItems();
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{id}")
    public String updateCartItem(@PathVariable("id") Long id, @RequestParam("quantity") int quantity) {
        cart.update(id, quantity);
        cart.calculateTotalAmount();
        return "redirect:/cart";
    }
}

