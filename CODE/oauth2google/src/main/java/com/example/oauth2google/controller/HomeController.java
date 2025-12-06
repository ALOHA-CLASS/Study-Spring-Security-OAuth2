package com.example.oauth2google.controller;

import com.example.oauth2google.service.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomOAuth2User oauth2User, Model model) {
        if (oauth2User != null) {
            model.addAttribute("user", oauth2User.getUser());
        }
        return "profile";
    }
}

