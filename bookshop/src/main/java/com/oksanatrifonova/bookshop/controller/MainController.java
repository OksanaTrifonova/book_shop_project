package com.oksanatrifonova.bookshop.controller;


import com.oksanatrifonova.bookshop.component.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
@AllArgsConstructor
public class MainController {
    public static final String CART_ITEM_COUNT = "cartItemCount";
    public static final String HOME = "home";
    public static final String REDIRECT = "redirect:/";
    public static final String ACCOUNT = "account";
    public static final String CONTACTS = "contacts";
    public static final String LOGIN = "login";
    private Cart cart;

    @GetMapping("/")
    public String viewHome(Model model) {
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);
        return HOME;
    }

    @GetMapping("/login")
    public String viewLogin(Principal principal, Model model) {
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);
        if (principal != null) {
            return REDIRECT;
        }
        return LOGIN;
    }


    @GetMapping("/contacts")
    public String viewContacts() {
        return CONTACTS;
    }

    @GetMapping("/account")
    public String viewAccount(Model model) {
        int cartItemCount = cart.getItemCount();
        model.addAttribute(CART_ITEM_COUNT, cartItemCount);
        return ACCOUNT;
    }

}