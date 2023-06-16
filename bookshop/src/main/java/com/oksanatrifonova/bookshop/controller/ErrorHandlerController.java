package com.oksanatrifonova.bookshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandlerController implements org.springframework.boot.web.servlet.error.ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

        if (statusCode == HttpStatus.NOT_FOUND.value() || statusCode == HttpStatus.FORBIDDEN.value() ||
                exception instanceof MethodArgumentNotValidException ||
                statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value() || exception instanceof NumberFormatException) {
            model.addAttribute("errorMessage", "Sorry !");
            model.addAttribute("errorDescription", "It looks like you’ve stumbled across a page that doesn’t exist on our site.");
            model.addAttribute("errorResolution", "For now, try checking the URL and pressing refresh or simply return to");
            return "error404";
        }

        return "error";
    }

}
