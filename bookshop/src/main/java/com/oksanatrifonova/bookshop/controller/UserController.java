package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.dto.AppUserDto;

import com.oksanatrifonova.bookshop.mapper.AppUserMapper;
import com.oksanatrifonova.bookshop.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {
    private AppUserServiceImpl userService;
    private AppUserMapper userMapper;


    @GetMapping("/login/register")
    public String showRegistrationForm(Model model) {
        AppUserDto userDto = new AppUserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/login/register")
    public String registerUser(@ModelAttribute("user") @Validated AppUserDto userDto,
                               BindingResult bindingResult,
                               Model model) {

        if (userService.emailExists(userDto.getEmail())) {
            bindingResult.rejectValue("email", "error.email",
                    "There is already an account registered with the same email");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            return "registration";
        }
        userService.registerUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/account/personal-details")
    public String viewPersonalDetails(Model model) {
        AppUser user = userService.getCurrentUser();

        AppUserDto userDto = userMapper.mapToUserDto(user);
        model.addAttribute("personalDetails", userDto);
        return "personal-details";
    }

    @PostMapping("/account/personal-details")
    public String addPersonalDetails(@ModelAttribute("personalDetails") AppUserDto userDto,
                                     Principal principal, BindingResult bindingResult, Model model) {
        String email = principal.getName();
        AppUser existingUser = userService.findUserByEmail(email);
        userService.updatePersonalDetails(existingUser, userDto);
        userService.saveUser(existingUser);
        return "redirect:/account/personal-details?success";
    }

}
