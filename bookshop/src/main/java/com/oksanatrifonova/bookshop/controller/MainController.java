package com.oksanatrifonova.bookshop.controller;

//import com.oksanatrifonova.bookshop.service.UserDetailsImpl;
//import com.oksanatrifonova.bookshop.dto.UserDetailsImpl;
import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.ItemDto;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class MainController {
    @Autowired
    private Cart cart;

    @GetMapping("/")
    public String home(Model model) {
        int cartItemCount = cart.getCart().stream()
                        .mapToInt(ItemDto::getQuantity)
                                .sum();
        model.addAttribute("cartItemCount",cartItemCount);
        return "home";
    }

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        return "login";
    }


    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/account")
    public String account(Model model){
        int cartItemCount = cart.getCart().stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
        model.addAttribute("cartItemCount",cartItemCount);
        return "account";
    }

}