package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.dto.BookOrderDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.entity.BookOrder;
import com.oksanatrifonova.bookshop.exception.BookValidationException;
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
import java.util.List;

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
            String userName = principal.getName();
            AppUser existingUser = userService.findUserByEmail(userName);
            BookOrder order = orderService.placeOrder(existingUser);
            model.addAttribute(ORDER, order);
            return ORDER_CONFIRMATION;
        } catch (BookValidationException ex) {
            model.addAttribute(MESSAGE, ex.getMessage());
            AppUser user = userService.getCurrentUser();
            AppUserDto userDto = userMapper.mapToUserDto(user);
            model.addAttribute(PERSONAL_DETAILS, userDto);
            return DETAILS_USER;
        }

    }

    @GetMapping("/account/orders")
    public String viewOrders(Model model, Principal principal) {
        String userName = principal.getName();
        AppUser user = userService.findUserByEmail(userName);
        List<BookOrderDto> orderDtoList = orderService.getOrdersForUser(user);
        model.addAttribute(ORDER_DETAILS, orderDtoList);
        return ORDER_LIST;
    }

    @GetMapping("/account/order/{orderId}")
    public String viewOrderDetails(@PathVariable("orderId") Long orderId, Model model) {
        BookOrderDto orderDto = orderService.getOrderById(orderId);
        model.addAttribute(ORDER, orderDto);
        return ORDER_LIST_DETAILS;
    }

}

