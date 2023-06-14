package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.exception.BookValidationException;
import com.oksanatrifonova.bookshop.mapper.AppUserMapper;
import com.oksanatrifonova.bookshop.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@AllArgsConstructor
public class UserController {
    private static final String ERROR = "error";
    private static final String USER = "user";
    private static final String REGISTRATION = "registration";
    private static final String REDIRECT_TO_LOGIN = "redirect:/login";
    private static final String PERSONAL_DETAILS = "personalDetails";
    private static final String VIEW_PERSONAL_DETAILS = "personal-details";
    private static final String SUCCESS_ADD_PERSONAL_DETAILS = "redirect:/account/personal-details?success";
    private AppUserServiceImpl userService;
    private AppUserMapper userMapper;


    @GetMapping("/login/register")
    public String showRegistrationForm(Model model) {
        if (model.containsAttribute(ERROR)) {
            String error = (String) model.getAttribute(ERROR);
            model.addAttribute(ERROR, error);
        }
        AppUserDto userDto = new AppUserDto();
        model.addAttribute(USER, userDto);
        return REGISTRATION;
    }

    @PostMapping("/login/register")
    public String registerUser(@ModelAttribute("user") @Validated AppUserDto userDto,
                               Model model) {
        try {
            userService.registerUser(userDto);
            return REDIRECT_TO_LOGIN;
        } catch (BookValidationException e) {
            model.addAttribute(ERROR, e.getMessage());
            return REGISTRATION;
        }
    }

    @GetMapping("/account/personal-details")
    public String viewPersonalDetails(Model model) {
        AppUser user = userService.getCurrentUser();
        AppUserDto userDto = userMapper.mapToUserDto(user);
        model.addAttribute(PERSONAL_DETAILS, userDto);
        return VIEW_PERSONAL_DETAILS;
    }

    @PostMapping("/account/personal-details")
    public String addPersonalDetails(@ModelAttribute(PERSONAL_DETAILS) AppUserDto userDto,
                                     Principal principal) {
        String email = principal.getName();
        AppUser existingUser = userService.findUserByEmail(email);
        userService.updatePersonalDetails(existingUser, userDto);
        userService.saveUser(existingUser);
        return SUCCESS_ADD_PERSONAL_DETAILS;
    }

}
