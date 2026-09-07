package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Home page after login
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}