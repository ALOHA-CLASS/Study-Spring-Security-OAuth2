package com.example.oauth2sociallogin.controller;

import com.example.oauth2sociallogin.oauth2.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomOAuth2User oauth2User, Model model) {
        if (oauth2User != null) {
            model.addAttribute("email", oauth2User.getEmail());
            model.addAttribute("name", oauth2User.getName());
            model.addAttribute("role", oauth2User.getRole().getTitle());
            model.addAttribute("attributes", oauth2User.getAttributes());
        }
        return "dashboard";
    }
}

