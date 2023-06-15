package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.exception.BookshopException;
import com.oksanatrifonova.bookshop.mapper.AppUserMapper;
import com.oksanatrifonova.bookshop.service.AppUserServiceImpl;
import com.oksanatrifonova.bookshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {
    private static final String ERROR = "error";
    private static final String USER = "user";
    private static final String USERS_PATH = "users";
    private static final String USER_ADD_VIEW = "user-add";
    private static final String ADMIN_PANEL = "admin-panel";
    private static final String MANAGER_PANEL = "manager-panel";
    private static final String REDIRECT_USERS_PATH = "redirect:/" + USERS_PATH;
    private static final String REGISTRATION = "registration";
    private static final String REDIRECT_TO_LOGIN = "redirect:/login";
    private static final String PERSONAL_DETAILS = "personalDetails";
    private static final String VIEW_PERSONAL_DETAILS = "personal-details";
    private static final String SUCCESS_ADD_PERSONAL_DETAILS = "redirect:/account/personal-details?success";
    private static final String USER_ORDERS = "userOrders";
    private static final String USER_ORDER = "user-order";
    private OrderService orderService;
    private AppUserServiceImpl userService;
    private AppUserMapper userMapper;

    @GetMapping("/login/register")
    public String showRegistrationForm(Model model) {
        if (model.containsAttribute(ERROR)) {
            model.addAttribute(ERROR, model.getAttribute(ERROR));
        }
        model.addAttribute(USER, new AppUserDto());
        return REGISTRATION;
    }

    @PostMapping("/login/register")
    public String registerUser(@ModelAttribute("user") @Validated AppUserDto userDto,
                               Model model) {
        try {
            userService.registerUser(userDto);
            return REDIRECT_TO_LOGIN;
        } catch (BookshopException e) {
            model.addAttribute(ERROR, e.getMessage());
            return REGISTRATION;
        }
    }

    @GetMapping("/account/personal-details")
    public String viewPersonalDetails(Model model) {
        model.addAttribute(PERSONAL_DETAILS, userMapper.mapToUserDto(userService.getCurrentUser()));
        return VIEW_PERSONAL_DETAILS;
    }

    @PostMapping("/account/personal-details")
    public String addPersonalDetails(@ModelAttribute(PERSONAL_DETAILS) AppUserDto userDto,
                                     Principal principal) {
        userService.updatePersonalDetails(userService.findUserByEmail(principal.getName()), userDto);
        userService.saveUser(userService.findUserByEmail(principal.getName()));
        return SUCCESS_ADD_PERSONAL_DETAILS;
    }

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
            model.addAttribute(ERROR, model.getAttribute(ERROR));
        }
        model.addAttribute(USERS_PATH, userService.findActiveUsersDto());
        return USERS_PATH;
    }

    @GetMapping("/user/add")
    public String viewUserAddForm(Model model) {
        if (model.containsAttribute(ERROR)) {
            model.addAttribute(ERROR, model.getAttribute(ERROR));
        }
        model.addAttribute(USER, new AppUserDto());
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
        } catch (BookshopException e) {
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
        } catch (BookshopException e) {
            model.addAttribute(ERROR, e.getMessage());
            model.addAttribute(USERS_PATH, userService.findActiveUsersDto());
            return USERS_PATH;
        }
    }

    @GetMapping("/user/{userId}/orders")
    public String viewUserOrders(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute(USER, userService.findUserById(userId));
        model.addAttribute(USER_ORDERS, orderService.getOrdersForUser(userService.findUserById(userId)));
        return USER_ORDER;
    }
}



