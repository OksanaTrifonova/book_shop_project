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

import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@AllArgsConstructor
public class EmployeeController {
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
        model.addAttribute("users", activeUsers);
        return "users";
    }

    @GetMapping("/user/add")
    public String viewUserAddForm(Model model) {
        return "user-add";
    }

    @PostMapping("/users/set-role")
    public String updateUserRole(@RequestParam("userId") Long userId, @ModelAttribute("role") String newRole) {
        userService.updateUserRole(userId, newRole);
        return "redirect:/users";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("userDto") AppUserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user-add";
        }
        try {
            userService.addUser(userDto);
            return "redirect:/users";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.userDto", e.getMessage());
            return "users";
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
