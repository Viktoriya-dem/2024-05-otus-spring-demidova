package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showAuthorizationForm() {
        return "login/login-page";
    }

    @GetMapping("/login-fail")
    public String loginFail(Model model) {
        model.addAttribute("loginFail", true);
        return "login/login-page";
    }

}
