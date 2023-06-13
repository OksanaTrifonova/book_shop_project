package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.dto.BookOrderDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.service.AppOrderService;
import com.oksanatrifonova.bookshop.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private static final String USER_ADD_VIEW = "user-add";

    private static final String REDIRECT_USERS_PATH = "redirect:/" + USERS_PATH;
    private AppUserServiceImpl userService;
    private AppOrderService orderService;

    @GetMapping("/admin")
    public String viewAdminPanel() {
        return "admin-panel";
    }

    @GetMapping("/manager")
    public String viewManagerPanel() {
        return "manager-panel";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<AppUserDto> activeUsers = userService.findActiveUsersDto();
        model.addAttribute(USERS_PATH, activeUsers);
        return USERS_PATH;
    }

    @GetMapping("/user/add")
    public String viewUserAddForm() {
        return USER_ADD_VIEW;
    }

    @PostMapping("/users/set-role")
    public String updateUserRole(@RequestParam("userId") Long userId, @ModelAttribute("role") String newRole) {
        userService.updateUserRole(userId, newRole);
        return REDIRECT_USERS_PATH;
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("userDto") AppUserDto userDto, BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            return USER_ADD_VIEW;
        }
        try {
            userService.addUser(userDto);
            return REDIRECT_USERS_PATH;
        } catch (IllegalArgumentException e) {
            if (userService.emailExists(userDto.getEmail())) {
                model.addAttribute("message",
                        "There is already an account registered with the same email");
                model.addAttribute("user", userDto);
                return USER_ADD_VIEW;
            }
            return USERS_PATH;
        }
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        try {
            userService.deleteUser(id);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error-delete-last-admin";
        }
    }


    @GetMapping("/user/{userId}/orders")
    public String viewUserOrders(@PathVariable("userId") Long userId, Model model) {
        AppUser user = userService.findUserById(userId);
        if (user == null) {
            return "error";
        }
        List<BookOrderDto> userOrders = orderService.getOrdersForUser(user);
        model.addAttribute("user", user);
        model.addAttribute("userOrders", userOrders);
        return "user-order";
    }
}
