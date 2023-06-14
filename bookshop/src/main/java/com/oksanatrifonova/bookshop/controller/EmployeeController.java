package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.dto.BookOrderDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.exception.BookValidationException;
import com.oksanatrifonova.bookshop.service.OrderService;
import com.oksanatrifonova.bookshop.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@AllArgsConstructor
public class EmployeeController {
    private static final String USERS_PATH = "users";
    private static final String USER = "user";
    private static final String USER_ADD_VIEW = "user-add";
    private static final String ADMIN_PANEL = "admin-panel";
    private static final String MANAGER_PANEL = "manager-panel";
    private static final String ERROR = "error";
    private static final String USER_ORDERS = "userOrders";
    private static final String USER_ORDER = "user-order";

    private static final String REDIRECT_USERS_PATH = "redirect:/" + USERS_PATH;
    private AppUserServiceImpl userService;
    private OrderService orderService;

    @GetMapping("/admin")
    public String viewAdminPanel() {
        return ADMIN_PANEL;
    }

    @GetMapping("/manager")
    public String viewManagerPanel() {
        return MANAGER_PANEL;
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        if (model.containsAttribute(ERROR)) {
            String error = (String) model.getAttribute(ERROR);
            model.addAttribute(ERROR, error);
        }
        List<AppUserDto> activeUsers = userService.findActiveUsersDto();
        model.addAttribute(USERS_PATH, activeUsers);
        return USERS_PATH;
    }

    @GetMapping("/user/add")
    public String viewUserAddForm(Model model) {
        if (model.containsAttribute(ERROR)) {
            String error = (String) model.getAttribute(ERROR);
            model.addAttribute(ERROR, error);
        }
        AppUserDto userDto = new AppUserDto();
        model.addAttribute(USER, userDto);
        return USER_ADD_VIEW;
    }

    @PostMapping("/users/set-role")
    public String updateUserRole(@RequestParam("userId") Long userId, @ModelAttribute("role") String newRole) {
        userService.updateUserRole(userId, newRole);
        return REDIRECT_USERS_PATH;
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("userDto") AppUserDto userDto,
                          Model model) {
        try {
            userService.addUser(userDto);
            return REDIRECT_USERS_PATH;
        } catch (BookValidationException e) {
            model.addAttribute(ERROR, e.getMessage());
            model.addAttribute(USER, userDto);
            return USER_ADD_VIEW;
        }
    }


    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        try {
            userService.deleteUser(id);
            return REDIRECT_USERS_PATH;
        } catch (BookValidationException e) {
            model.addAttribute(ERROR, e.getMessage());
            List<AppUserDto> users = userService.findActiveUsersDto();
            model.addAttribute(USERS_PATH, users);
            return USERS_PATH;
        }
    }


    @GetMapping("/user/{userId}/orders")
    public String viewUserOrders(@PathVariable("userId") Long userId, Model model) {
        AppUser user = userService.findUserById(userId);
        model.addAttribute(USER, user);
        List<BookOrderDto> userOrders = orderService.getOrdersForUser(user);
        model.addAttribute(USER_ORDERS, userOrders);
        return USER_ORDER;
    }
}
