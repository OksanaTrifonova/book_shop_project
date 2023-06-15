package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.exception.BookshopException;
import com.oksanatrifonova.bookshop.mapper.AppUserMapper;
import com.oksanatrifonova.bookshop.service.AppUserServiceImpl;
import com.oksanatrifonova.bookshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class OrderController {
    public static final String ORDER_DETAILS = "orderDetailsList";
    public static final String ORDER = "order";
    public static final String ORDER_CONFIRMATION = "order-confirmation";
    public static final String PERSONAL_DETAILS = "personalDetails";
    public static final String DETAILS_USER = "personal-details";
    public static final String LOGIN = "login";
    public static final String ORDER_LIST_DETAILS = "order-list-details";
    public static final String ORDER_LIST = "order-list";
    public static final String MESSAGE = "message";
    private final AppUserServiceImpl userService;
    private AppUserMapper userMapper;
    private OrderService orderService;


    @PostMapping("/place-order")
    public String placeOrder(Model model, Principal principal) {
        try {
            if (principal == null) {
                model.addAttribute(MESSAGE, "You must be logged in to place an order");
                return LOGIN;
            }
            model.addAttribute(ORDER, orderService.placeOrder(userService.findUserByEmail(principal.getName())));
            return ORDER_CONFIRMATION;
        } catch (BookshopException ex) {
            model.addAttribute(MESSAGE, ex.getMessage());
            model.addAttribute(PERSONAL_DETAILS, userMapper.mapToUserDto(userService.getCurrentUser()));
            return DETAILS_USER;
        }
    }

    @GetMapping("/account/orders")
    public String viewOrders(Model model, Principal principal) {
        model.addAttribute(ORDER_DETAILS, orderService.getOrdersForUser(userService.findUserByEmail(principal.getName())));
        return ORDER_LIST;
    }

    @GetMapping("/account/order/{orderId}")
    public String viewOrderDetails(@PathVariable("orderId") Long orderId, Model model) {
        model.addAttribute(ORDER, orderService.getOrderById(orderId));
        return ORDER_LIST_DETAILS;
    }
}

